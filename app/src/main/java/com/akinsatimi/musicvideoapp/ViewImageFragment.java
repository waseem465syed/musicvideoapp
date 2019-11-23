package com.akinsatimi.musicvideoapp;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.akinsatimi.musicvideoapp.MyLibraries.CommonRoutines;
import com.akinsatimi.musicvideoapp.MyLibraries.MyConstants;

import java.io.File;


public class ViewImageFragment extends Fragment {

    private ImageView imgViewPicture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_image, container, false);


        //pop message to ask user to allow permission
        CommonRoutines.permissionGranted(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE, "READ_EXTERNAL_STORAGE");

        imgViewPicture = v.findViewById(R.id.imgViewPicture);

        File imgFile = new File(MyConstants.IMAGEPATH);

        if (imgFile.exists()) {

            //get the bitmap of image
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            //show the bitmap
            imgViewPicture.setImageBitmap(myBitmap);

        } else {

            //display message in both snackbar and toast
            CommonRoutines.displayMessage(getContext(), null, "No image found", 0, Toast.LENGTH_LONG);
        }

        return v;
    }

}
