package com.bignerdranch.android.criminalintent;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class CriminalIntentJSONSerializer {
    private Context mContext;
    private String mFileName;

    public CriminalIntentJSONSerializer(Context c , String f)
    {
        mContext = c;
        mFileName = f;
    }

    //loading crimes from fileSystem
    public ArrayList<Crime> loadCrimes()throws IOException,JSONException
    {
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;
        try
        {
            //open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                //line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            //parse the json using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //build the array of crimes from JSONObjects
            for (int i = 0 ; i<array.length() ; i++)
                crimes.add(new Crime (array.getJSONObject(i)));
        } catch (FileNotFoundException e){
            //ignore this , it happens when starting fresh
        }
        finally {
            if(reader != null)
                reader.close();
        }
        return crimes;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException
    {
        //build an array in json
        JSONArray array = new JSONArray();
        for(Crime c : crimes)
            array.put(c.toJSON());

        //write the file to disk
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFileName,Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        }
        finally {
            if(writer != null)
                writer.close();
        }
    }
}
