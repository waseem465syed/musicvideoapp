package com.akinsatimi.musicvideoapp;

import android.Manifest;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.akinsatimi.musicvideoapp.MyLibraries.CommonRoutines;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;

    //errormessage is used with Fab to display last error message
    private String errorMessage = "Thanks for downloading my app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    //open fragment
    private void openFragment(Fragment fragment)
    {
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.flFrame, fragment);

        //add to back stack
        ft.addToBackStack(null);

        //commit
        ft.commit();
    }

    //close all fragments
    private void closeAllFragments()
    {

        if (getSupportFragmentManager() == null) {
            return;
        }

        if (getSupportFragmentManager().getFragments().size() > 0) {

            FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(0);

            if (entry != null) {
                getSupportFragmentManager().popBackStackImmediate(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id)
        {
            case R.id.openMusicFragment:
                openFragment(new MusicFragment());
                break;

            case R.id.openTakeVideoFragment:

                //check user have camera
                if (CommonRoutines.permissionGranted(this, Manifest.permission.CAMERA, "Camera") &&
                        CommonRoutines.permissionGranted(this, Manifest.permission.RECORD_AUDIO, "RECORD_AUDIO")&&
                        CommonRoutines.permissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, "WRITE_EXTERNAL_STORAGE"))
                {
                    openFragment(new TakeVideoFragment());
                }
                else
                {
                    errorMessage = "Please enable camera, record audio and write external storage permissions to use device";
                    CommonRoutines.displayMessage(this, fab, errorMessage, Snackbar.LENGTH_LONG, Toast.LENGTH_LONG);
                }
                break;

            case R.id.openWatchVideoFragment:
                if (CommonRoutines.permissionGranted(this, Manifest.permission.CAMERA, "Camera") &&
                    CommonRoutines.permissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE, "READ_EXTERNAL_STORAGE"))
                {
                    openFragment(new ViewVideoFragment());
                }
                else
                {
                    errorMessage = "Please enable camera and read external storage permissions to use device";
                    CommonRoutines.displayMessage(this, fab, errorMessage, Snackbar.LENGTH_LONG, Toast.LENGTH_LONG);
                }
                break;

            case R.id.openViewImageFragment:

                if (CommonRoutines.permissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE, "READ_EXTERNAL_STORAGE"))
                {
                    openFragment(new ViewImageFragment());
                }
                else
                {
                    errorMessage = "Please enable read external storage permissions to use device";
                    CommonRoutines.displayMessage(this, fab, errorMessage, Snackbar.LENGTH_LONG, Toast.LENGTH_LONG);
                }
                break;

            case R.id.resetFragment:

                closeAllFragments();

                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {

        if (v == fab)
        {
            CommonRoutines.displayMessage(null, fab, errorMessage, Snackbar.LENGTH_SHORT, 0);
        }
    }
}
