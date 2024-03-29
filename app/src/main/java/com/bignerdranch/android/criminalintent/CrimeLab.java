package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

//SINGLETON
public class CrimeLab {
    private static final String FILENAME = "crimes.json";
    private CriminalIntentJSONSerializer mSerializer;

    private ArrayList<Crime> mCrimes;

    private static CrimeLab sCrimeLab; // automatically initialized to null
    private Context mAppContext;

    private CrimeLab(Context appContext)
    {
        mAppContext = appContext;
        mSerializer = new CriminalIntentJSONSerializer(mAppContext , FILENAME);
        //load crimes into the ArrayList rather than always creating a new one
        try {
            mCrimes =mSerializer.loadCrimes();
        } catch (Exception e){
            mCrimes = new ArrayList<Crime>();
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
            if(c.getmId().equals(id))
                return c;

        return null;
    }
    public void addCrime(Crime c)
    {
        mCrimes.add(c);
    }

    public void deleteCrime(Crime c)
    {
        mCrimes.remove(c);
    }

    //saving changes persistently in CrimeLab
    public boolean saveCrimes()
    {
        try
        {
            mSerializer.saveCrimes(mCrimes);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

