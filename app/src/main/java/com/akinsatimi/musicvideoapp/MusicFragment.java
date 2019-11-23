package com.akinsatimi.musicvideoapp;


import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment implements View.OnClickListener {

    private ImageButton imgBtnPlay;
    private ImageButton imgBtnStop;
    private ImageButton imgBtnPause;

    private Spinner spChoice;

    private MediaPlayer mPlayer;

    int selectedMusic = 0;

    public MusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_music, container, false);

        imgBtnStop = v.findViewById(R.id.imgBtnStop);
        imgBtnStop.setOnClickListener(this);

        imgBtnPlay = v.findViewById(R.id.imgBtnPlay);
        imgBtnPlay.setOnClickListener(this);

        imgBtnPause = v.findViewById(R.id.imgBtnPause);
        imgBtnPause.setOnClickListener(this);

        spChoice = v.findViewById(R.id.spChoice);

        spChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = (String) parent.getItemAtPosition(position);

                switch(item)
                {
                    case "1":
                        selectedMusic = R.raw.music1;
                        break;

                    case "2":
                        selectedMusic = R.raw.music2;
                        break;

                    default :
                        //do nothing
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.imgBtnPlay:
                //play music
                playMusic();
                break;

            case R.id.imgBtnPause:
                //pause music
                pauseMusic();
                break;

            case R.id.imgBtnStop:
                //stop music
                stopMusic();
                break;
        }
    }

    private void stopMusic() {

        if (mPlayer != null)
        {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void pauseMusic() {

        if(mPlayer != null && selectedMusic != 0){
            mPlayer.pause();
        }
    }

    private void playMusic() {

        if(mPlayer == null && selectedMusic != 0){
            //if new, create object
            mPlayer = MediaPlayer.create(getContext(), selectedMusic);
        }

        mPlayer.start();
    }
}
