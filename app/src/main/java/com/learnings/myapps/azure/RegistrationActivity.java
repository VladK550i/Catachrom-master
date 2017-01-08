package com.learnings.myapps.azure;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.common.util.concurrent.ListenableFuture;
import com.learnings.myapps.azure.Entity.User;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;


public class RegistrationActivity extends AppCompatActivity {

    private MobileServiceClient mClient;
    private View mRegistrationFormView;
    private View mProgressView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private UserRegistrationTask mRegTask;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        try {
            mClient = new MobileServiceClient(
                    "https://mobileapp99.azurewebsites.net",
                    this
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mEmailView = (EditText) findViewById(R.id.editText);
        mPasswordView = (EditText) findViewById(R.id.editText2);

        Button b_insert = (Button) findViewById(R.id.button);
        b_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String login = mEmailView.getText().toString();
//                String password = mPasswordView.getText().toString();
//                InsertIntoTodo(login, password);
                attemptRegistration();
            }
        });

        mRegistrationFormView = findViewById(R.id.registration_form);
        mProgressView = findViewById(R.id.registration_progress);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void attemptRegistration() {
        if (mRegTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            //Vle 08.01.2017 RegFix01 -->
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    final List<User> users;
                    try {
                        users = mClient.getTable(User.class).where().field("Login").eq(email).execute().get(); //.and().field("Password").eq(password).execute().get();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!users.isEmpty()) {
                                    mEmailView.setError(getString(R.string.error_exist_login));
                                    mEmailView.requestFocus();
                                }
                                else {
                                    showProgress(true);
                                    mRegTask = new UserRegistrationTask(email, password);
                                    mRegTask.execute((Void) null);
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute((Void)null);
//VLE regFix <--
        }
    }

    private boolean isEmailValid(String email) {

        return email.toLowerCase().matches("[a-z][a-z0-9-._]*[a-z0-9]*[@][a-z]+[.][a-z]+");
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 7 && password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])\\S+");
        //ограничение на: 1 большая буква, 1 малая буква, 1 цифра, 8 символов

//        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegistrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegistrationFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegistrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegistrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean InsertIntoTodo(String login, String password) {


        User item = new User();
        item.setLogin(login);
        item.setPassword(password);
        ListenableFuture<User> future = mClient.getTable(User.class).insert(item);
        try {
            if (future.get() != null)
                return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            //return true;
        }
        return false;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Registration Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class UserRegistrationTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserRegistrationTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            return InsertIntoTodo(mEmail, mPassword);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegTask = null;
            showProgress(false);

            if (!success) {
                mEmailView.setError(getString(R.string.error_registration_failed));
                mEmailView.requestFocus();
            } else {
                Toast.makeText(RegistrationActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mRegTask = null;
            showProgress(false);
        }
    }
}
