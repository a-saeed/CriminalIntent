package com.bignerdranch.android.criminalintent;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    @Override
    public Dialog onCreateDialog(Bundle onSavedInstance)
    {
        //use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //create a new instance of datePickerDialog and return it
         DatePickerDialog dpd = new DatePickerDialog(getActivity(), this , year , month , day);
         dpd.setTitle(R.string.date_picker_title);
        return dpd;
    }

    public void onDateSet(DatePicker view , int year , int month , int day)
    {
        //do something with the date chosen by user

    }
}
