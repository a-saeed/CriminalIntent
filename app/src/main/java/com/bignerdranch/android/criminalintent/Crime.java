package com.bignerdranch.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

public class Crime
{
    public static final String JSON_ID = "id";
    public static final String JSON_TITLE = "title";
    public static final String JSON_SOLVED = "solved";
    public static final String JSON_DATE = "date";
    
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

    //saving an individual instance of crime in json.
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID , mId.toString());
        json.put(JSON_TITLE , mTitle);
        json.put(JSON_SOLVED , isSolved);
        json.put(JSON_DATE , mDate);
        return json;
    }
}
