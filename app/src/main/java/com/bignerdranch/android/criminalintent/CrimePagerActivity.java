package com.bignerdranch.android.criminalintent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

public class CrimePagerActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //instantiate viewPager and set it as the content view
        //(instead of creating new XMl layout file , we create this simple layout in code.)
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mCrimes = CrimeLab.get(this).getCrimes();

        //setting up the adapter
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                //return a CrimeFragment configured
                //to display the crime at that position
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getmId()) ;
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        //reaction when pages change
        //replace the activity's title with the title of the current item
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Crime crime = mCrimes.get(position);
                if(crime != null)
                    setTitle(crime.getmTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //show the item(crime) that was selected
        //instead of the first item in pagerAdapter
        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for (int i = 0 ; i < mCrimes.size() ; i++)
        {
            if(mCrimes.get(i).getmId() .equals(crimeId))
            {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }

}
