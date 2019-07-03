package com.bignerdranch.android.criminalintent;

import java.util.UUID;

public class Crime
{
    private UUID mId;
    private String mTitle;
    public Crime()
    {
        //Generate a unique identifier
        mId= UUID.randomUUID();
    }

    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
