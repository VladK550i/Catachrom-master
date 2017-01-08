package com.learnings.myapps.azure.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.learnings.myapps.azure.DataTransfer;
import com.learnings.myapps.azure.Entity.Account;
import com.learnings.myapps.azure.Entity.Bank;
import com.learnings.myapps.azure.Entity.BankOffer;
import com.learnings.myapps.azure.R;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.List;

import static com.learnings.myapps.azure.Fragments.DataContainer.mClient;

public class BankInfoActivity extends AppCompatActivity {

    Bank cur_bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_info);

        Intent intent = getIntent();
        String acc_id = intent.getStringExtra("id");
        GetBankInfoById(acc_id);
    }

    private void GetBankInfoById(final String acc_id) {
        final MobileServiceTable btable = mClient.getTable(Bank.class);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final List<Bank> bresponse = (List) btable.where()
                            .field("id")
                            .eq(acc_id).execute().get();
                    if (bresponse.size() != 1)
                        throw new Exception("Response returned more or less than 1 value");
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cur_bank = bresponse.get(0);

                                TextView tv_bankname = (TextView) findViewById(R.id.textView17);
                                TextView tv_bank_description = (TextView) findViewById(R.id.textView18);
                                TextView tv_deposit_sum = (TextView) findViewById(R.id.textView19);
                                TextView tv_credit_sum = (TextView) findViewById(R.id.textView20);
                                TextView tv_liquid_active = (TextView) findViewById(R.id.textView21);
                                TextView tv_authorized_capital = (TextView) findViewById(R.id.textView22);
                                tv_bankname.setText(cur_bank.getBankName());
                                tv_bank_description.setText(cur_bank.getBankDescription());
                                tv_deposit_sum.setText("Deposit summ:" + cur_bank.getLastQuartalDepositSum());
                                tv_credit_sum.setText("Credit summ:" + cur_bank.getLastQuartalCreditSum());
                                tv_liquid_active.setText("Liquid active:" + cur_bank.getLastQuartalLiquidActive());
                                tv_authorized_capital.setText("Authorized capital: " + cur_bank.getLastQuartalAuthorisedCapital());
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}
