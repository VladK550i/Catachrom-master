package com.learnings.myapps.azure.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.learnings.myapps.azure.DataFiller;
import com.learnings.myapps.azure.DataTransfer;
import com.learnings.myapps.azure.Entity.Account;
import com.learnings.myapps.azure.Entity.Bank;
import com.learnings.myapps.azure.Entity.BankOffer;
import com.learnings.myapps.azure.MainActivity;
import com.learnings.myapps.azure.R;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.learnings.myapps.azure.Fragments.DataContainer.mClient;
import static com.learnings.myapps.azure.Fragments.DataContainer.mEmail;


public class DepositsFragment extends Fragment implements DataTransfer {

    DataFiller filler;
    List<Account> list;

    List<Map<String, String>> data = new ArrayList<>();
    List<String> ids = new ArrayList<>();
    SimpleAdapter adapter;

    String bank_name, offer_name;

    public DepositsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Deposit manager -> Open a deposit");
        View v = inflater.inflate(R.layout.fragment_deposits, container, false);
        adapter = new SimpleAdapter(getContext(), data, android.R.layout.simple_list_item_2, new String[] {"main", "addit"},
                new int[] {android.R.id.text1,android.R.id.text2});
        SelectAccounts(v);
        return v;
    }

    private void SelectAccounts(final View v) {
        final MobileServiceTable table = mClient.getTable(Account.class);
        final Button b = (Button) v.findViewById(R.id.button3);
        final View progressView = (View) v.findViewById(R.id.load_progress);
        final View introForm = (View) v.findViewById(R.id.intro_form);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Runnable emptyListRunnable = new Runnable() {
                        @Override
                        public void run() {
                            TextView tv = (TextView) v.findViewById(R.id.textView2);
                            tv.setText("You have no opened deposits yet.");
                            tv.setVisibility(View.VISIBLE);
                            b.setVisibility(View.VISIBLE);
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    MainActivity activity = (MainActivity) getActivity();
                                    activity.onNavigationItemSelected(activity.getNavigationView().getMenu().getItem(1));
                                }
                            });
                        }
                    };
                    Runnable populatedListRunnable = new Runnable() {
                        @Override
                        public void run() {
                            TextView tv = (TextView) v.findViewById(R.id.textView2);
                            tv.setVisibility(View.GONE);
                            b.setVisibility(View.GONE);
                        }
                    };
                    final List<Account> response = (List) table.where().field("LoginRefRecId").eq(mEmail).execute().get();
                    Runnable sendDataRunnable = new Runnable() {
                        @Override
                        public void run() {

                            final ListView lv = (ListView) v.findViewById(R.id.lv);
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(getContext(), DepositInfoActivity.class);
                                    intent.putExtra("id", ids.get(i));
                                    startActivity(intent);
                                }
                            });

                            data.clear();
                            for (Account account : response) {
                                FillAdapter(account);
                            }
                            progressView.setVisibility(View.GONE);
                            introForm.setVisibility(View.VISIBLE);
                        }
                    };

                    if (response == null || response.size() == 0) {
                        getActivity().runOnUiThread(emptyListRunnable);
                    } else {
                        getActivity().runOnUiThread(populatedListRunnable);
                    }
                    getActivity().runOnUiThread(sendDataRunnable);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void FillAdapter(final Account account) {
        filler = new DataFiller() {
            @Override
            public void FillIn(List l, Account curr_acc) {
                Date start_date = curr_acc.getDateFrom();
                Calendar end_calendar = GregorianCalendar.getInstance();
                end_calendar.setTime(start_date);
                end_calendar.add(Calendar.MONTH, curr_acc.getDepositTermMonth());
                String end_string = end_calendar.get(Calendar.DAY_OF_MONTH) + "."
                        + (end_calendar.get(Calendar.MONTH)+1) + "."
                        + end_calendar.get(Calendar.YEAR);

                Map<String, String> datum = new HashMap<>(2);
                datum.put("main", l.get(0) + " from " + l.get(1));
                datum.put("addit", "Summ: " + curr_acc.getStartFunds()
                        + "; Ends: " + end_string);
                data.add(datum);
                ids.add(account.id);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        final String id_offer = account.getBankOfferRefRecId();
        final String id_bank = account.getBankRefRecID();
        final MobileServiceTable otable = mClient.getTable(BankOffer.class);
        final MobileServiceTable btable = mClient.getTable(Bank.class);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final List response = (List) otable.where()
                            .field("id")
                            .eq(id_offer)
                            .select("OfferName").top(1).execute().get();
                    final List response2 = (List) btable.where()
                            .field("id")
                            .eq(id_bank)
                            .select("BankName").top(1).execute().get();

                    if (response.size() > 0)
                        offer_name = ((BankOffer) response.get(0)).getOfferName();
                    else
                        offer_name = "<null>";
                    if (response2.size() > 0)
                        bank_name = ((Bank) response2.get(0)).getBankName();
                    else
                        bank_name = "<null>";

                    List<String> l = new ArrayList<>(2);
                    l.add(offer_name); l.add(bank_name);
                    filler.FillIn(l, account);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void ReceiveData(Object data_sent) {
        list = (List) data_sent;
    }
}
