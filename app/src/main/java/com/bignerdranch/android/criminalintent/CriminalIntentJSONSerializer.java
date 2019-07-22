package com.bignerdranch.android.criminalintent;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
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
