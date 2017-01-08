package com.learnings.myapps.azure.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.EditText;

import com.learnings.myapps.azure.R;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private String TAG;


    public DatePickerFragment() {

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.TAG = getArguments().getString("type");
        // определяем текущую дату
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // создаем DatePickerDialog и возвращаем его
        Dialog picker = new DatePickerDialog(getActivity(), this,
                year, month, day);
        picker.setTitle("Choose date:");

        return picker;
    }
    @Override
    public void onStart() {
        super.onStart();
        Button nButton =  ((AlertDialog) getDialog())
                .getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText("Ready");

    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year,
                          int month, int day) {
        int new_month = month+1;
        if (TAG.equals("from")) {
            EditText et = (EditText) getActivity().findViewById(R.id.editText3);
            et.setText(day + "." + new_month + "." + year);
        }
        else if (TAG.equals("to")) {
            EditText et2 = (EditText) getActivity().findViewById(R.id.editText4);
            et2.setText(day + "." + new_month + "." + year);
        }
    }
}