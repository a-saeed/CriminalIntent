package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;


public class CrimeFragment extends Fragment {
    static final String EXTRA_CRIME_ID =
            "com.bignerdranch.android.criminalintent.crime_id";
    private static final String TAG = "CrimeFragment";
    //request code
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;
    private  Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    //Writing a newInstance(UUID)
    //any activity (or fragment) in need of this fragment
    //should call this method.
    public static CrimeFragment newInstance(UUID crimeId)
    {
        //create arguments bundle
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID , crimeId);
        //create fragment instance
        //attach arguments to the fragment
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return  fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieve the EXTRA (crimeId)
        //from fragment's arguments
        //instead directly from its hosting activity
        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        //use the EXTRA crimeId to fetch
        //the crime from CrimeLab.
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

        //tell the FragmentManager that this fragment
        // should receive a call to onCreateOptionsMenu(…).
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        //WIRE UP WIDGETS HERE
        //wiring crime tile EditText
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mCrime.setmTitle(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        })
        ;

        //wiring crime date Button
        //show a date picker dialog when clicked
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment dialog =
                        DatePickerFragment.newInstance(mCrime.getmDate());
                FragmentManager fm = getActivity().getSupportFragmentManager();
                //once the DatePickerFragment is created
                //set crimeFragment as its Target fragment.
                dialog.setTargetFragment(CrimeFragment.this , REQUEST_DATE);
                dialog.show(fm , "date");
            }
        });

        //wiring crime solved Checkbox
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //set the crime solved property
                mCrime.setSolved(isChecked);
            }
        });

        //wiring the image button
        mPhotoButton = (ImageButton)v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(i , REQUEST_PHOTO);
            }
        });

        //wiring the imageView
        mPhotoView = (ImageView)v.findViewById(R.id.crime_imageView);
        //show a larger version of the image when clicked
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo p = mCrime.getmPhoto();
                if (p == null)
                    return;

                FragmentManager fm = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path).show(fm , "image");
            }
        });

        return v;
    }

    //responding to the dialog , or the camera.
    //overriding onActivityResult() to retrieve the extra,
    //set the date on the crime
    //and refresh the text of date button.
    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent incomingIntent)
    {
        if(resultCode != Activity.RESULT_OK)
            return;
        if(requestCode == REQUEST_DATE)
        {
            Date date = (Date) incomingIntent
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            updateDate();
        }
        else if (requestCode == REQUEST_PHOTO)
        {
            //create a new photo object and attach it to the crime.
            String filename = incomingIntent.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if(filename != null)
            {

                Photo p =new Photo(filename);
                mCrime.setmPhoto(p);
                //ensure that the image will be visible when user returns
                //from crimeCameraActivity
                showPhoto();
            }
        }
    }

    private void updateDate() {
        //format the date
        String formattedDate =
                DateFormat.getDateInstance(DateFormat.FULL).format(mCrime.getmDate());
        mDateButton.setText(formattedDate);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

    //enabling deletion of a crime from its detail view as well from the list
    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu , inflater);
        inflater.inflate(R.menu.crime_list_item_context , menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                CrimeLab.get(getActivity()).saveCrimes();
                Intent i = new Intent(getActivity() , CrimeListActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_item_done:
                Intent c = new Intent(getActivity() , CrimeListActivity.class);
                startActivity(c);
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void showPhoto()
    {
        //(Re)set the image button's image based on our photo
        Photo p = mCrime.getmPhoto();
        BitmapDrawable b = null;
        if (p != null)
        {
            String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity() , path);
        }
        mPhotoView.setImageDrawable(b);
    }

    @Override
    public void onStart()
    {
        //have the photo ready as soon as CrimeFragment's view becomes visible.
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop()
    {
        //unloading the image
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

    //create four strings , piece them together and
    //return a complete crime report.
    private String getCrimeReport()
    {
        String solvedString = null;
        if (mCrime.isSolved())
            solvedString = getString(R.string.crime_report_solved);
        else
            solvedString = getString(R.string.crime_report_unsolved);

        String suspect = mCrime.getmSuspect();
        if (suspect == null)
            suspect = getString(R.string.crime_report_no_suspect);
        else
            suspect = getString(R.string.crime_report_subject);

        String report = getString(R.string.crime_report , mCrime.getmTitle() , mCrime.getmDate() ,
                solvedString , suspect);

        return  report;
    }

}
