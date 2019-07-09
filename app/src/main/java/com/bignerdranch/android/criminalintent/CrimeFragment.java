package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;


public class CrimeFragment extends Fragment {
    public static final String EXTRA_CRIME_ID =
            "com.bignerdranch.android.criminalintent.crime_id";
    private  Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    //Writing a newInstance(UUID)
    //any activity in need of this fragment
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
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getmDate().toString());
        mDateButton.setEnabled(false);
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




}
