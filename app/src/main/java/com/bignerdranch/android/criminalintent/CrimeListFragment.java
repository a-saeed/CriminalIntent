package com.bignerdranch.android.criminalintent;

import android.os.Bundle;

import androidx.fragment.app.ListFragment;

import java.util.ArrayList;

public class CrimeListFragment extends ListFragment {
    private ArrayList<Crime> mCrimes;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //set title of the activity that will host this fragment
        getActivity().setTitle(R.string.crimes_title);
        //access list of crimes stored in crimeLab
        mCrimes = CrimeLab.get(getActivity()).getCrimes();
    }
}
