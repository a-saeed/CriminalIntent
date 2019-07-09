package com.bignerdranch.android.criminalintent;

import androidx.fragment.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        //retrieving the extra from this activity's intent
        //pass it into
        //CrimeFragment.newInstance(UUID)
        UUID crimeId = (UUID)getIntent()
                .getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);

        return CrimeFragment.newInstance(crimeId);
    }
}
