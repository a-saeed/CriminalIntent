package com.bignerdranch.android.criminalintent;

import android.content.Context;

//SINGLETON
public class CrimeLab {
    private static CrimeLab sCrimeLab; // automatically initialized to null
    private Context mAppContext;

    private CrimeLab(Context appContext)
    {
        mAppContext = appContext;
    }

    public static CrimeLab get (Context c)
    {
        if(sCrimeLab==null)
            sCrimeLab = new CrimeLab(c.getApplicationContext());

        return sCrimeLab;
    }
}

