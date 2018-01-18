package com.example.al_pc.realtimegps.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service {
    public static final String BROADCAST_ACTION = "LocationService";

    public final static String KEY_PROVIDER = "Provider";
    public final static String KEY_LAT = "Latitude";
    public final static String KEY_LNG = "Longitude";

    public LocationManager locationManager;
    public MyLocationListener listener;

    private Intent intent;


    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.e("No permision", "ok");
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);

        return Service.START_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("", "onBind");
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            Log.e("*******", "Location changed");
            Log.e("*******", "Latitude= " + loc.getLatitude());
            Log.e("*******", "Longitude= " + loc.getLongitude());

            intent.putExtra(KEY_LAT, loc.getLatitude());
            intent.putExtra(KEY_LNG, loc.getLongitude());
            intent.putExtra(KEY_PROVIDER, loc.getProvider());
            sendBroadcast(intent);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(getApplicationContext(), "Status Changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


    }
}