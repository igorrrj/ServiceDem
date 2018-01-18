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

import com.example.al_pc.realtimegps.data.web.Api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends Service {
    public static final String BROADCAST_ACTION = "LocationService";

    public final static String KEY_PROVIDER = "Provider";
    public final static String KEY_LAT = "Latitude";
    public final static String KEY_LNG = "Longitude";

    public LocationManager locationManager;
    public MyLocationListener listener;

    private List<HashMap<String, Object>> data;

    private Intent intent;

    private Api api;

    @Override
    public void onCreate() {
        super.onCreate();

        intent = new Intent(BROADCAST_ACTION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();

        data = new ArrayList<>();
        api = Api.getInstance();

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
        public void onLocationChanged(Location position) {

            String transportId = "GolfTest";
            HashMap<String, Object> currentData = new HashMap<>();
            currentData.put("transportId", transportId);
            currentData.put("speed", position.getSpeed());
            currentData.put("latitude", position.getLatitude());
            currentData.put("longitude", position.getLongitude());
            currentData.put("date", position.getTime());

            data.add(currentData);

            api.getQuery().postLocation(data).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

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
}