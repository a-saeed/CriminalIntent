package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;


public class CrimeFragment extends Fragment {
    static final String EXTRA_CRIME_ID =
            "com.bignerdranch.android.criminalintent.crime_id";
    //request code
    private static final int REQUEST_DATE = 0;
    private  Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    //Writing a newInstance(UUID)
    //any activity (or fragment) in need of this fragment
    //should call this method.
    public static CrimeFragment newInstance(UUID crimeId)
    {
        //create arguments bundle
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID , crimeId);
        //create fragment instance
        //attach arguments to the fragment
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return  fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieve the EXTRA (crimeId)
        //from fragment's arguments
        //instead directly from its hosting activity
        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        //use the EXTRA crimeId to fetch
        //the crime from CrimeLab.
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        //WIRE UP WIDGETS HERE
        //wiring crime tile EditText
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mCrime.setmTitle(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        })
        ;

        //wiring crime date Button
        //show a date picker dialog when clicked
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment dialog =
                        DatePickerFragment.newInstance(mCrime.getmDate());
                FragmentManager fm = getActivity().getSupportFragmentManager();
                //once the DatePickerFragment is created
                //set crimeFragment as its Target fragment.
                dialog.setTargetFragment(CrimeFragment.this , REQUEST_DATE);
                dialog.show(fm , "date");
            }
        });

        //wiring crime solved Checkbox
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //set the crime solved property
                mCrime.setSolved(isChecked);
            }
        });
        return v;
    }

    //responding to the dialog
    //overriding onActivityResult() to retrieve the extra,
    //set the date on the crime
    //and refresh the text of date button.
    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent incomingIntent)
    {
        if(resultCode != Activity.RESULT_OK)
            return;
        if(requestCode == REQUEST_DATE)
        {
            Date date = (Date) incomingIntent
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        //format the date
        String formattedDate =
                DateFormat.getDateInstance(DateFormat.FULL).format(mCrime.getmDate());
        mDateButton.setText(formattedDate);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }
}
