package com.learnings.myapps.azure.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learnings.myapps.azure.R;


public class QuickCalculationFragment extends Fragment {


    public QuickCalculationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Deposit manager -> Quick calculation");
        View v = inflater.inflate(R.layout.fragment_quick_calculation, container, false);
        return v;
    }
}
