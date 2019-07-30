package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class CrimeCameraFragment extends Fragment {
    public static final String EXTRA_PHOTO_FILENAME = "com.bignerdranch.android.criminalintent.photo_filename";

    private static final String TAG = "CrimeCameraFragment";
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private View mProgressContainer;
    //needed to implement camera callbacks

    //occurs when the camera captures the picture
    //but before the image data is processed and available.
    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            //display the progress indicator
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };

    //dealing with the captured JPEG picture
    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback()
    {
        public void onPictureTaken (byte[] data , Camera camera)
        {
            //create fileName
            String fileName = UUID.randomUUID().toString() + ".jpg";
            //save the jpeg data to disk
            FileOutputStream os = null;
            boolean success = true;
            try {
                os = getActivity().openFileOutput(fileName , Context.MODE_PRIVATE);
                os.write(data);
            } catch (Exception e) {
                Log.e(TAG , "error writing to file" , e);
                success = false;
            } finally {
                try {
                    if (os != null)
                        os.close();
                } catch (Exception e){
                    Log.e(TAG , "error closing file" , e);
                    success = false;
                }
            }
            //set the photo fileName on the on the result intent
            if (success) {
                Intent i = new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME , fileName);
                getActivity().setResult(Activity.RESULT_OK , i);
            }
            else {
                getActivity().setResult(Activity.RESULT_CANCELED);
            }
            getActivity().finish();
        }
    };


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
                if (mCamera != null)
                    mCamera.takePicture(mShutterCallback , null , mJpegCallback);
            }
        });

        //setting the frameLayout as invisible
        //as long as the "TAKE" button isn't clicked.
        mProgressContainer = v.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);

        mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
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
                        mCamera.setPreviewDisplay(surfaceHolder);
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
                parameters.setPreviewSize(s.width, s.height);

                //set the picture size
                s = getBestSupportedSize(parameters.getSupportedPictureSizes(), w, h);
                parameters.setPictureSize(s.width, s.height);

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
