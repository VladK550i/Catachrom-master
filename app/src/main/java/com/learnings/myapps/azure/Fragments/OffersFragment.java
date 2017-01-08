package com.learnings.myapps.azure.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learnings.myapps.azure.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OffersFragment extends Fragment {


    public OffersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Offers");
        View v = inflater.inflate(R.layout.fragment_offers, container, false);
        return v;
    }

}
