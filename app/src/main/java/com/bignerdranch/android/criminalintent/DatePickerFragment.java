package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    public static final String EXTRA_DATE =
            "com.bignerdranch.android.criminalintent.date";
    //date instance used to retrieve fragment arguments
    //(the EXTRA being sent to this fragment)
    private Date mDate;


    //Writing a newInstance(UUID)
    //any activity (or fragment) in need of this fragment
    //should call this method.
    static DatePickerFragment newInstance(Date date)
    {
        //create arguments bundle
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE , date);
        //create fragment instance
        //attach arguments to the fragment
        DatePickerFragment dpf = new DatePickerFragment();
        dpf.setArguments(args);

        return dpf;
    }

    //calling back to the Target fragment(CrimeFragment)
    //a method that creates an intent,
    //puts the date on it as an EXTRA
    //then calls CrimeFragment.onActivityResult().
    private void sendResult(int resultCode)
    {
        if(getTargetFragment()==null)
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_DATE , mDate);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode() , resultCode , i);
    }

    @Override
    public Dialog onCreateDialog(Bundle onSavedInstance)
    {
        //retrieve date argument
        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
        //initialize DatePicker dialog to the time of crime
        final Calendar c = Calendar.getInstance();
        c.setTime(mDate);
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
        // Translate year, month, day into a Date object using a calendar
        mDate =  new GregorianCalendar(year , month , day).getTime();
        // Update argument to preserve selected value on rotation
        getArguments().putSerializable(EXTRA_DATE , mDate);

        sendResult(Activity.RESULT_OK);


    }
}
