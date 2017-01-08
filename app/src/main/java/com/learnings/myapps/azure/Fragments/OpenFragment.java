package com.learnings.myapps.azure.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.learnings.myapps.azure.Entity.Account;
import com.learnings.myapps.azure.Entity.Bank;
import com.learnings.myapps.azure.Entity.BankOffer;
import com.learnings.myapps.azure.R;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;

import static com.learnings.myapps.azure.Fragments.DataContainer.mClient;
import static com.learnings.myapps.azure.Fragments.DataContainer.mEmail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OpenFragment extends Fragment {

    private ArrayList<String> banks = new ArrayList<>();
    private ArrayList<String> offers = new ArrayList<>();
    private ArrayAdapter<String> banks_adapter;
    private ArrayAdapter<String> offers_adapter;
    private String cur_bank_id;
    private String cur_offer_id;

    public OpenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_add_account, container, false);

//        try {
//            mClient = new MobileServiceClient(
//                    "https://mobileapp99.azurewebsites.net",
//                    this.getContext()
//            );
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

        getActivity().setTitle("Open a deposit");

        final Spinner banks_spinner = (Spinner) v.findViewById(R.id.spinner);
        banks_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, banks);
        banks_spinner.setAdapter(banks_adapter);
        banks_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String cur_bank = banks_adapter.getItem(banks_spinner.getSelectedItemPosition());
                GetBankID(cur_bank);

                LoadOffers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        LoadBanks();

        final Spinner offers_spinner = (Spinner) v.findViewById(R.id.spinner2);
        offers_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, offers);
        offers_spinner.setAdapter(offers_adapter);
        offers_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String cur_offer = offers_adapter.getItem(offers_spinner.getSelectedItemPosition());
                GetOfferID(cur_offer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final EditText startamount = (EditText) v.findViewById(R.id.editText5);
        final Button btn_datePicker1 = (Button) v.findViewById(R.id.button4);
        btn_datePicker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePicker = new DatePickerFragment();
                Bundle b = new Bundle();
                b.putString("type", "from");
                datePicker.setArguments(b);
                datePicker.show(getActivity().getSupportFragmentManager(), "DatePicker");
            }
        });
        final EditText dateFrom = (EditText) v.findViewById(R.id.editText3);
        final EditText et_term = (EditText) v.findViewById(R.id.editText4);

        final Button btn_ok = (Button) v.findViewById(R.id.button6);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                String[] date1 = dateFrom.getText().toString().split("\\.");
                c.set(Integer.valueOf(date1[2]), Integer.valueOf(date1[1])-1, Integer.valueOf(date1[0]));
                Date date_from = c.getTime();

                int term = Integer.valueOf(et_term.getText().toString());

                InsertIntoAccount(mEmail, cur_bank_id, cur_offer_id, Integer.valueOf(startamount.getText().toString()),
                        date_from, term);
            }
        });
        return v;
    }

    private void GetBankID(final String bank) {

        final MobileServiceTable table = mClient.getTable(Bank.class);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final List response = (List) table.where()
                                                      .field("BankName")
                                                      .eq(bank)
                                                      .select("id").top(1).execute().get();
                    if (response.size() > 0)
                        cur_bank_id = ((Bank)response.get(0)).id;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void GetOfferID(final String offer) {

        final MobileServiceTable table = mClient.getTable(BankOffer.class);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final List response = (List) table.where()
                            .field("OfferName")
                            .eq(offer)
                            .select("id").top(1).execute().get();
                    if (response.size() > 0)
                        cur_offer_id = ((BankOffer) response.get(0)).id;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void LoadBanks() {
        final MobileServiceTable table = mClient.getTable(Bank.class);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final List<Bank> response = (List<Bank>) table.execute().get();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            banks.clear();
                            for (Bank bank : response) {
                                banks.add(bank.getBankName());
                            }
                            banks_adapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void LoadOffers() {
        final MobileServiceTable table = mClient.getTable(BankOffer.class);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final List<BankOffer> response = (List<BankOffer>) table.where()
                                                                            .field("BankRefRecID")
                                                                            .eq(cur_bank_id).execute().get();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            offers.clear();
                            if (response.size() > 0) {
                                for (BankOffer offer : response) {
                                    offers.add(offer.getOfferName());
                                }
                                offers_adapter.notifyDataSetChanged();
                            }
                            else
                                Toast.makeText(getContext(), "Unfortunately at the moment this bank has no offers on deposits", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void InsertIntoAccount(String id_login, String id_bank, String id_bankoffer, Integer startfunds,
                                   Date dateFrom, int term) {
        Account item = new Account();
        item.setLoginRefRecId(id_login);
        item.setBankRefRecID(id_bank);
        item.setBankOfferRefRecId(id_bankoffer);
        item.setStartFunds(startfunds);
        item.setDateFrom(dateFrom);
        item.setDepositTermMonth(term);

        mClient.getTable(Account.class).insert(item, new TableOperationCallback<Account>() {
            public void onCompleted(Account entity, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Something's wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
