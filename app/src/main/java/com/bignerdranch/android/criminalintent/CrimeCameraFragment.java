package com.bignerdranch.android.criminalintent;

import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.List;

public class CrimeCameraFragment extends Fragment {
    private static final String TAG = "CrimeCameraFragment";
    private Camera mCamera;
    private SurfaceView mSurfaceView;

    @Override
    @SuppressWarnings("deprecation")
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
        final SurfaceHolder holder = mSurfaceView.getHolder();
        // setType() and SURFACE_TYPE_PUSH_BUFFERS are both deprecated,
        // but are required for Camera preview to work on pre-3.0 devices.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //implementing SurfaceHolder.CallBack
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                //tell the camera to use this surface as its preview area
                try{
                    if(mCamera != null)
                        mCamera.setPreviewDisplay(holder);
                }
                catch (IOException exception){
                    Log.e(TAG , "Error setting up preview display" , exception);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {
                if (mCamera == null)
                    return;
                //the surface has changed size ; update the camera preview size
                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
                parameters.setPreviewSize(w , h);
                mCamera.setParameters(parameters);

                try{
                    mCamera.startPreview();
                }
                catch (Exception e){
                    Log.e(TAG , "couldn't start preview" , e);
                    mCamera.release();
                    mCamera = null;
                }

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                //we can no longer display on this surface , so stop the preview
                if (mCamera != null)
                    mCamera.stopPreview();

            }
        });
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

    // A simple algorithm to get the largest size available.
    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height)
    {
        Camera.Size bestSize = sizes.get(0);
        int largestArea = width * height;
        for (Camera.Size s : sizes)
        {
            int area = width * height;
            if (area > largestArea)
            {
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize; }
}
