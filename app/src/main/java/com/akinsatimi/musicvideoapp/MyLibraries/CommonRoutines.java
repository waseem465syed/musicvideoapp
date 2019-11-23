package com.akinsatimi.musicvideoapp.MyLibraries;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class CommonRoutines {

    public static boolean cameraHardwareExists(Context context)
    {
//        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
//        {
//            return true;
//        }
//        else
//        {
//            return false;
//        }

        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    public static void displayMessage(Context context, View view, String message, int snackBarduration, int toastDuration)
    {

        if (context != null) {
            Toast.makeText(context, message, toastDuration).show();
        }

        if (view != null) {
            Snackbar.make(view, message, snackBarduration)
                    .setAction("Action", null).show();
        }

    }

    public static boolean permissionGranted(Activity activity, String permission, String permissionName)
    {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{permission}, 50);

            displayMessage(activity, null, "You need to allow " + permissionName + " permission to use this app", 0, Toast.LENGTH_LONG);

            return false;
        }

        return true;
    }

}
