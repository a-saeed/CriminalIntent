package com.bignerdranch.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

public class Crime
{
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";
    private static final String JSON_PHOTO = "photo";
    private static final String JSON_SUSPECT = "suspect";
    
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean isSolved;
    private Photo mPhoto;
    private String mSuspect;


    public Crime()
    {
        //Generate a unique identifier
        mId= UUID.randomUUID();
        mDate = new Date();
    }

    //adding a constructor that takes a JSOnObject
    //to load data from local files.
    public Crime(JSONObject json) throws JSONException
    {
        mId = UUID.fromString(json.getString(JSON_ID));
        if(json.has(JSON_TITLE))
            mTitle = json.getString(JSON_TITLE);
        isSolved = json.getBoolean(JSON_SOLVED);
        mDate = new Date (json.getLong(JSON_DATE));

        if (json.has(JSON_PHOTO))
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        if (json.has(JSON_SUSPECT))
            mSuspect = json.getString(JSON_SUSPECT);
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

    public Photo getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(Photo mPhoto) {
        this.mPhoto = mPhoto;
    }
    public String getmSuspect() {
        return mSuspect;
    }

    public void setmSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
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
        json.put(JSON_DATE , mDate.getTime());
        if (mPhoto != null)
            json.put(JSON_PHOTO , mPhoto.toJSON());
        json.put(JSON_SUSPECT , mSuspect);
        return json;
    }
}
