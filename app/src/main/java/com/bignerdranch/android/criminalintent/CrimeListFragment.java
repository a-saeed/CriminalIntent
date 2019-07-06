package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

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

        //create an instance of ArrayAdapter<T>
        // and make it the adapter for CrimeListFragment’s ListView
        ArrayAdapter<Crime> adapter =
                new ArrayAdapter<Crime>(getActivity(),
                        android.R.layout.simple_expandable_list_item_1,
                        mCrimes);
        //setListAdapter(ListAdapter) method is a
        // ListFragment convenience method that you can use to
        // set the adapter of the implicit ListView managed by CrimeListFragment.
        setListAdapter(adapter);
    }


}
