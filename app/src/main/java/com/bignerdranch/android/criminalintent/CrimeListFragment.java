package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.ListFragment;

import java.text.DateFormat;
import java.util.ArrayList;

public class CrimeListFragment extends ListFragment {
    private static final String TAG="CrimeListFragment";
    private ArrayList<Crime> mCrimes;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //tell the FragmentManager that this fragment
        // should receive a call to onCreateOptionsMenu(…).
        setHasOptionsMenu(true);

        //set title of the activity that will host this fragment
        getActivity().setTitle(R.string.crimes_title);

        //access list of crimes stored in crimeLab
        mCrimes = CrimeLab.get(getActivity()).getCrimes();

        //using the custom CrimeAdapter
        //instead of basic ArrayAdapter
        CrimeAdapter adapter = new CrimeAdapter(mCrimes);

        //setListAdapter(ListAdapter) method is a
        // ListFragment convenience method that you can use to
        // set the adapter of the implicit ListView managed by CrimeListFragment.
        setListAdapter(adapter);
    }

    //list item click listener
    @Override
    public void onListItemClick(ListView l , View v , int position , long id)
    {
        //CrimeAdapter can hold Crime objects
        //no need to cast to Crime.
        //get the crime from the adapter
        Crime c = ((CrimeAdapter) getListAdapter()).getItem(position);
        //start CrimePagerActivity
        //CrimeListFragment uses the getActivity() method
        //to pass its hosting activity as the context object
        //that the intent constructor requires.
        Intent i = new Intent(getActivity() , CrimePagerActivity.class);
        //passing the mCrimeId as an EXTRA so that
        //CrimePagerActivity data for a specific Crime.
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID , c.getmId());
        startActivity(i);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //inform list view's adapter that the data set has changed
        //(or may have changed) so it can refetch the data
        //and reload the list
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    //creating options menu in action bar
    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu , inflater);
        inflater.inflate(R.menu.fragment_crime_list , menu);

    }

    //responding to item selected in options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //if the item selected is the (new crime) item
        switch (item.getItemId())
        {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i = new Intent(getActivity(),CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getmId());
                startActivityForResult(i,0);
                startActivity(i);
                return true;
                default:
                    super.onOptionsItemSelected(item);

        }
        return false;
    }
    //a custom adapter to show crime-specific data in list view.
    private class CrimeAdapter extends ArrayAdapter<Crime>
    {
        //calling super class constructor is required
        //to properly hook up our data set of (Crime)s.
        public CrimeAdapter(ArrayList<Crime> crimes)
        {
            super(getActivity(),0 , crimes);
        }

        @Override
        public View getView(int position , View convertView , ViewGroup parent)
        {
            //if we weren't given a view , inflate one.
            if(convertView==null)
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_crime , null);

            //configure the view for this crime
            Crime c = getItem(position);

            TextView titleTextView =
                    (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getmTitle());

            TextView dateTextView =
                    (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
            //format date
            String formattedDate =
                    DateFormat.getDateInstance(DateFormat.FULL).format(c.getmDate());
            dateTextView.setText(formattedDate);

            CheckBox solvedCheckBox =
                    (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());

            //return the view object to list view
            return convertView;
        }
    }

}
