package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

//SINGLETON
public class CrimeLab {
    private ArrayList<Crime> mCrimes;

    private static CrimeLab sCrimeLab; // automatically initialized to null
    private Context mAppContext;

    private CrimeLab(Context appContext)
    {
        mAppContext = appContext;
        mCrimes = new ArrayList<Crime>();
        for (int i=0 ; i<100 ;i++)
        {
            Crime c = new Crime();
            c.setmTitle("crime #"+i);
            c.setSolved(i%2 ==0); //every other one
            mCrimes.add(c);
        }
    }

    public static CrimeLab get (Context c)
    {
        if(sCrimeLab==null)
            sCrimeLab = new CrimeLab(c.getApplicationContext());

        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id)
    {
        for (Crime c: mCrimes)
            if(c.getmId() == id)
                return c;

        return null;
    }
}

