package com.learnings.myapps.azure;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;

import java.net.MalformedURLException;
import java.util.List;


public class MainScreenActivity extends AppCompatActivity implements DataTransfer {

    private MobileServiceClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Button b_insert = (Button) findViewById(R.id.button);
        Button b_select = (Button) findViewById(R.id.button2);

        final EditText editText = (EditText) findViewById(R.id.editText);
        final TextView textView = (TextView) findViewById(R.id.textView);

        b_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InsertIntoTodo(editText.getText().toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        b_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectFromToDo();
            }
        });

    }

    private void InsertIntoTodo(String text) throws MalformedURLException {
        ToDoItem item = new ToDoItem();
        item.Text = text;
        mClient.getTable(ToDoItem.class).insert(item, new TableOperationCallback<ToDoItem>() {
            public void onCompleted(ToDoItem entity, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    Toast.makeText(getBaseContext(), "I made it", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "Something's wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void SelectFromToDo() {
        final MobileServiceTable table = mClient.getTable(ToDoItem.class);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final List<ToDoItem> response = (List<ToDoItem>) table.execute().get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ReceiveData(response);
                        }
                    });
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void ReceiveData(Object data_sent) {
        List<ToDoItem> list = (List<ToDoItem>) data_sent;
        Toast.makeText(getBaseContext(), list.toString(), Toast.LENGTH_SHORT).show();
    }
}
