package com.learnings.myapps.azure.Fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.learnings.myapps.azure.DataTransfer;
import com.learnings.myapps.azure.Entity.Account;
import com.learnings.myapps.azure.Entity.Bank;
import com.learnings.myapps.azure.R;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.learnings.myapps.azure.Fragments.DataContainer.mClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class BanksFragment extends Fragment implements DataTransfer {

    List<Bank> banks;
    List<String> banks_namelist;
    List<Map<String, String>> data = new ArrayList<>();
    SimpleAdapter adapter;
    ListView lv;

    public BanksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Banks");

        View v = inflater.inflate(R.layout.fragment_banks, container, false);
        Toast.makeText(getContext(), "Banks fragment", Toast.LENGTH_SHORT).show();

        lv = (ListView) v.findViewById(R.id.listView);
        adapter = new SimpleAdapter(getContext(), data, android.R.layout.simple_list_item_2, new String[] {"main", "addit"},
                new int[] {android.R.id.text1,android.R.id.text2});
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), BankInfoActivity.class);
                intent.putExtra("id", banks.get(i).id);
                startActivity(intent);
            }
        });

        SelectBanks(v);
        return v;
    }

    private void SelectBanks(final View v) {
        final MobileServiceTable table = mClient.getTable(Bank.class);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Runnable emptyListRunnable = new Runnable() {
                        @Override
                        public void run() {
                            TextView tv = (TextView) v.findViewById(R.id.textView);
                            tv.setText("We have no banks yet.");
                        }
                    };
                    Runnable populatedListRunnable = new Runnable() {
                        @Override
                        public void run() {
                            TextView tv = (TextView) v.findViewById(R.id.textView);
                            tv.setVisibility(View.INVISIBLE);
                        }
                    };
                    final List response = (List) table.execute().get();
                    Runnable sendDataRunnable = new Runnable() {
                        @Override
                        public void run() {

                            ReceiveData(response);
                        }
                    };

                    if (response == null || response.size() == 0) {
                        getActivity().runOnUiThread(emptyListRunnable);
                    } else {
                        getActivity().runOnUiThread(populatedListRunnable);
                    }
                    getActivity().runOnUiThread(sendDataRunnable);

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void ReceiveData(Object data_sent) {
        banks = (List) data_sent;

        for (Bank bank: banks) {
            Map<String, String> datum = new HashMap<>(2);
            datum.put("main", bank.getBankName());
            datum.put("addit", bank.getBankRecId());
            data.add(datum);
        }
        adapter.notifyDataSetChanged();
    }
}
