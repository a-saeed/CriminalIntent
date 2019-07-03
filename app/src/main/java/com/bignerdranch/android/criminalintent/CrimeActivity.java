package com.bignerdranch.android.criminalintent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class CrimeActivity extends FragmentActivity {
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        /*retrieve an existing fragment by its
        *container id
        *fragmentContainer is the FrameLayout.*/
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment==null)
        {
            fragment = new CrimeFragment();
            /* “Create a new fragment transaction, include one add operation in it,
             * and then commit it.”
             * CrimeActivity is now hosting a CrimeFragment.
             */
            fm.beginTransaction()
                    .add(R.id.fragmentContainer,fragment)
                    .commit();
       }
    }
}
