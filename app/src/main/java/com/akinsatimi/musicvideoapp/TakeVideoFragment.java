package com.akinsatimi.musicvideoapp;


import android.Manifest;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.akinsatimi.musicvideoapp.MyLibraries.CommonRoutines;
import com.akinsatimi.musicvideoapp.MyLibraries.MyConstants;

import java.io.FileOutputStream;
import java.io.IOException;


public class TakeVideoFragment extends Fragment implements View.OnClickListener, SurfaceHolder.Callback {

    private Button btnRecord;
    private Button btnPicture;

    private SurfaceView svVideo;

    private Camera camera;

    private SurfaceHolder holder;


    public TakeVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_take_video, container, false);

        btnRecord = v.findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(this);

        btnPicture = v.findViewById(R.id.btnPicture);
        btnPicture.setOnClickListener(this);

        svVideo = v.findViewById(R.id.svVideo);

        //pop message to ask user to allow permission
        CommonRoutines.permissionGranted(getActivity(), Manifest.permission.CAMERA, "Camera");


        holder = svVideo.getHolder();
//        holder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//
//            }
//        });

        holder.addCallback(this);


        return v;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        releaseCameraAndPreview();

        try {
            //open camera
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (camera != null) {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                camera.setDisplayOrientation(90);
            }

            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            camera.startPreview();
        } else {
            //display message in toast
            CommonRoutines.displayMessage(getContext(), null, "No camera hardware found", 0, Toast.LENGTH_SHORT);
        }

    }

    private void releaseCameraAndPreview() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        refreshCamera();
    }

    private void refreshCamera() {
        if (holder.getSurface() != null) {
            camera.stopPreview();

            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            camera.startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCameraAndPreview();
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btnPicture:

                if (CommonRoutines.permissionGranted(getActivity(), Manifest.permission.CAMERA, "Camera") &&
                        CommonRoutines.permissionGranted(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, "WRITE_EXTERNAL_STORAGE")) {
                    takePicture();
                } else {
                    //display message in both snackbar and toast
                    CommonRoutines.displayMessage(getContext(), btnPicture, "Please enable camera and write external storage permissions to use device", Snackbar.LENGTH_LONG, Toast.LENGTH_LONG);
                }
                break;

            case R.id.btnRecord:

                if (CommonRoutines.permissionGranted(getActivity(), Manifest.permission.CAMERA, "Camera") &&
                        CommonRoutines.permissionGranted(getActivity(), Manifest.permission.RECORD_AUDIO, "RECORD_AUDIO")) {
                    recordVideo();
                } else {
                    //display message in both snackbar and toast
                    CommonRoutines.displayMessage(getContext(), btnPicture, "Please enable camera and record audio permissions to use device", Snackbar.LENGTH_LONG, Toast.LENGTH_LONG);
                }
                break;
        }
    }

    private void disableButtons() {
        btnRecord.setEnabled(false);
        btnPicture.setEnabled(false);
    }

    private void enableButtons() {
        btnRecord.setEnabled(true);
        btnPicture.setEnabled(true);
    }

    private void takePicture() {

        if (camera != null) {

            //disable the buttons
            disableButtons();

            //display message in both toast
            CommonRoutines.displayMessage(getContext(), null, "Snapping picture, pls wait...", 0, Toast.LENGTH_SHORT);

            camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {

                    try {

                        //get the image snapped
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                        //save the image
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(MyConstants.IMAGEPATH));

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        enableButtons();

                        //display message in toast
                        CommonRoutines.displayMessage(getContext(), null, "Snap completed", 0, Toast.LENGTH_SHORT);
                    }
                }
            });
        } else {
            //display message in both snackbar and toast
            CommonRoutines.displayMessage(getContext(), btnPicture, "No camera found on your device", Snackbar.LENGTH_LONG, Toast.LENGTH_LONG);
        }
    }

    private void recordVideo() {


        if (camera != null) {

            //disable the buttons
            disableButtons();

            //unlock the camera
            camera.unlock();

            //create a new instance of MediaRecorder
            final MediaRecorder recorder = new MediaRecorder();

            //set the recorder's camera
            recorder.setCamera(camera);

            //set the video and audio source
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            //set the save format
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            //set the video and audio encoder
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

            //set the output file location
            recorder.setOutputFile(MyConstants.VIDEOPATH);

            //prepare the recording
            try {
                recorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("OluError", "Error: " + e);
            }

            //display message toast message
            CommonRoutines.displayMessage(getContext(), null, "Recording, pls wait...", 0, Toast.LENGTH_LONG);

            //start the recording
            recorder.start();

            //create an handler
            Handler handle = new Handler();

            handle.postDelayed(new Runnable() {
                @Override
                public void run() {

                    //lock the camera
                    camera.lock();

                    //stop the recording
                    recorder.stop();

                    //release
                    recorder.release();

                    //display message toast message
                    CommonRoutines.displayMessage(getContext(), null, "Finished recording.", 0, Toast.LENGTH_LONG);

                    //enable buttons
                    enableButtons();
                }
            }, 10 * 1000);


        } else {
            //display message in both snackbar and toast
            CommonRoutines.displayMessage(getContext(), btnPicture, "No camera found on your device", Snackbar.LENGTH_LONG, Toast.LENGTH_LONG);
        }
    }
}
