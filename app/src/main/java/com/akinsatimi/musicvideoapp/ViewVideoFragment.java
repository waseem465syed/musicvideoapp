package com.akinsatimi.musicvideoapp;


import android.Manifest;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.akinsatimi.musicvideoapp.MyLibraries.CommonRoutines;
import com.akinsatimi.musicvideoapp.MyLibraries.MyConstants;

public class ViewVideoFragment extends Fragment {

    private VideoView vvVideo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_video, container, false);

        vvVideo = v.findViewById(R.id.vvVideo);

        //pop message to ask user to allow permission
        CommonRoutines.permissionGranted(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE, "READ_EXTERNAL_STORAGE");

        //set the window for video
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);

        //set the vidoe path
        vvVideo.setVideoPath(MyConstants.VIDEOPATH);

        //keep the video screen on
        vvVideo.setKeepScreenOn(true);

        //create an instance of MediaController
        MediaController mediaController = new MediaController(getActivity());

        mediaController.setAnchorView(vvVideo);

        //set the media controller on vvVideo
        vvVideo.setMediaController(mediaController);

        //request the focus
        vvVideo.requestFocus();

        //start the video
        vvVideo.start();


        return v;
    }

}
