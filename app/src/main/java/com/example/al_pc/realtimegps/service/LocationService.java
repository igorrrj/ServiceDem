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
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.al_pc.realtimegps.aplication.data.local.db.DBRepo;
import com.example.al_pc.realtimegps.aplication.data.web.Api;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service {
    public static final String BROADCAST_ACTION = "LocationService";

    public final static String KEY_PROVIDER = "Provider";
    public final static String KEY_LAT = "Latitude";
    public final static String KEY_LNG = "Longitude";

    public LocationManager locationManager;
    public MyLocationListener listener;

    private Intent intent;

    private Handler mHandler;
    private Timer mTimer;

    private Api api;
    private DBRepo db;

    @Override
    public void onCreate() {
        super.onCreate();

        intent = new Intent(BROADCAST_ACTION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();

        mHandler = new Handler();
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }

        mTimer.scheduleAtFixedRate(new ServerUploadTimerTask(), 0, 60 * 1000);
        api = Api.getInstance();
        db = DBRepo.getInstance(getApplicationContext());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.e("LocationService", "No permission");
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
        public void onLocationChanged(Location position) {

            String transportId = "GolfTest";
            HashMap<String, Object> currentData = new HashMap<>();
            currentData.put(DBRepo.TRANSPORT_ID, transportId);
            currentData.put(DBRepo.SPEED, position.getSpeed());
            currentData.put(DBRepo.LATITUDE, position.getLatitude());
            currentData.put(DBRepo.LONGITUDE, position.getLongitude());
            currentData.put(DBRepo.DATE, position.getTime());


            db.addLocation(currentData).subscribe();

            Log.e("*******", "Location changed");
            Log.e("*******", "Latitude= " + position.getLatitude());
            Log.e("*******", "Longitude= " + position.getLongitude());

            intent.putExtra(KEY_LAT, position.getLatitude());
            intent.putExtra(KEY_LNG, position.getLongitude());
            intent.putExtra(KEY_PROVIDER, position.getProvider());
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

    class ServerUploadTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(

                    () -> db.getLocationData()
                            .subscribe(

                                    data -> api.sendLocation(data)

                                            .subscribe(

                                                    () -> db.removeLocationData().subscribe(),
                                                    Throwable::printStackTrace
                                            ),

                                    Throwable::printStackTrace

                            )
            );
        }
    }

}