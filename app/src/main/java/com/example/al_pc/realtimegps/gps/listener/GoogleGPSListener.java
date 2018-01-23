package com.example.al_pc.realtimegps.gps.listener;


import android.content.Context;
import android.location.Location;

import com.example.al_pc.realtimegps.aplication.data.local.db.DBRepo;
import com.example.al_pc.realtimegps.gps.callback.IGPSCallback;
import com.google.android.gms.location.LocationListener;

import java.util.HashMap;

public class GoogleGPSListener implements LocationListener {

    private Context context;

    private IGPSCallback callback;

    public GoogleGPSListener(Context context, IGPSCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void onLocationChanged(Location location) {

        String transportId = "GolfTest";
        HashMap<String, Object> currentData = new HashMap<>();
        currentData.put(DBRepo.TRANSPORT_ID, transportId);
        currentData.put(DBRepo.SPEED, location.getSpeed());
        currentData.put(DBRepo.LATITUDE, location.getLatitude());
        currentData.put(DBRepo.LONGITUDE, location.getLongitude());
        currentData.put(DBRepo.DATE, location.getTime());

        callback.onLocationChanged(currentData, location.getProvider());
    }

}
