package com.bignerdranch.android.criminalintent;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class CrimeCameraFragment extends Fragment {
    private Camera mCamera;
    private SurfaceView mSurfaceView;

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup parent ,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_crime_camera , parent , false);

        Button takePictureButton = (Button)v.findViewById(R.id.crime_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                getActivity().finish();
            }
        });

        mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //target api >= 9
        //acquiring a camera instance
        mCamera = Camera.open(0);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        //releasing the camera when not in use
        if(mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }
}
