package com.bignerdranch.android.criminalintent;

import androidx.fragment.app.Fragment;

public class CrimeCameraActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment()
    {
        return new CrimeCameraFragment();
    }
}
