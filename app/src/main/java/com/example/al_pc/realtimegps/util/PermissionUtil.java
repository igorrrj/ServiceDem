package com.example.al_pc.realtimegps.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;

public class PermissionUtil {

    private static String[] networkPermissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    private static String[] locationkPermissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private static boolean checkForGrant(Activity activity, String[] grantStr) {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> tmpPermissions = new ArrayList<>();
            for (String permission : grantStr) {
                if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                    tmpPermissions.add(permission);
            }
            if (tmpPermissions.size() > 0) {
                ActivityCompat.requestPermissions(activity, tmpPermissions.toArray(new String[0]), 101);
                return false;
            }
            return true;
        } else return true;
    }


    public static boolean isNetworkGranted(Activity activity) {
        return checkForGrant(activity, networkPermissions);
    }

    public static boolean isLocationGranted(Activity activity) {
        return checkForGrant(activity, locationkPermissions);
    }


}