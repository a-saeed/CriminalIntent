package com.bignerdranch.android.criminalintent;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

public class ImageFragment extends DialogFragment {

    public static final String EXTRA_IMAGE_PATH = "com.bignerdranch.android.criminalintent.image_path";
    private ImageView mImageView;

    public static ImageFragment newInstance(String imagePath)
    {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH , imagePath);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        //achieve a minimalistic look
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE , 0);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup parent , Bundle savedInstanceState)
    {
        //create an image view from scratch
        mImageView = new ImageView(getActivity());
        //retrieve file path from fragment arguments
        String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        //scale the image
        BitmapDrawable image = PictureUtils.getScaledDrawable(getActivity(),path);

        mImageView.setImageDrawable(image);

        return mImageView;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        PictureUtils.cleanImageView(mImageView);
    }
}
