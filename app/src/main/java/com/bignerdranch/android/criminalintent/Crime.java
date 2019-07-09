package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime
{
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean isSolved;
    public Crime()
    {
        //Generate a unique identifier
        mId= UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getmId() {
        return mId;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    //overriding toString() to populate
    //TextView in ArrayAdapter with
    //useful information
    @Override
    public String toString()
    {
        return mTitle;
    }
}
