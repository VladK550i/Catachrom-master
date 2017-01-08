package com.learnings.myapps.azure.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.learnings.myapps.azure.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DepositInfo_taxesFragment extends Fragment {


    public DepositInfo_taxesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deposit_info_taxes, container, false);

        Bundle b = getArguments();
        Toast.makeText(getContext(), b.getString("region"), Toast.LENGTH_SHORT).show();

        return v;
    }

}
