package com.example.al_pc.realtimegps.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.al_pc.realtimegps.aplication.data.local.db.DBRepo;
import com.example.al_pc.realtimegps.aplication.data.web.Api;
import com.example.al_pc.realtimegps.gps.GoogleServiceGeolocation;

import java.util.Timer;
import java.util.TimerTask;

public class LocationServiceNew extends Service {
    public static final String BROADCAST_ACTION = "LocationServiceNew";

    public final static String KEY_LAT = "Latitude";
    public final static String KEY_LNG = "Longitude";

    private Intent sendIntent;

    private Handler mHandler;
    private Timer mTimer;

    private Api api;
    private DBRepo db;

    private GoogleServiceGeolocation googleServiceGeolocation;

    public static void toast(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sendIntent = new Intent(BROADCAST_ACTION);

        mHandler = new Handler();
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }

        api = Api.getInstance();
        db = DBRepo.getInstance(getApplicationContext());

        googleServiceGeolocation = new GoogleServiceGeolocation(getApplicationContext());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.e("LocationService", "No permission");
        }

        mTimer.scheduleAtFixedRate(new ServerUploadTimerTask(), 60 * 1000, 60 * 1000);

        googleServiceGeolocation.startLocationListener((model, provider) -> {

            db.addLocation(model).subscribe();

            Log.e("*******", "Location changed");
            Log.e("*******", "Latitude= " + model.get(DBRepo.LATITUDE));
            Log.e("*******", "Longitude= " + model.get(DBRepo.LONGITUDE));

            sendIntent.putExtra(KEY_LAT, (Double) model.get(DBRepo.LATITUDE));
            sendIntent.putExtra(KEY_LNG,(Double) model.get(DBRepo.LONGITUDE));

            sendBroadcast(sendIntent);

        });

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
        googleServiceGeolocation.stopLocationListener();
        mTimer.cancel();

    }


    class ServerUploadTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(

                    () -> db.getLocationData()
                            .subscribe(

                                    data -> api.sendLocation(data)

                                            .subscribe(

                                                    () -> {

                                                        db.removeLocationData().subscribe();
                                                        toast(getApplicationContext(), "send data to server successfully");

                                                    },
                                                    throwable -> {

                                                        toast(getApplicationContext(), "send data to server error");
                                                        throwable.printStackTrace();

                                                    }
                                            ),

                                    Throwable::printStackTrace

                            )
            );
        }
    }

}