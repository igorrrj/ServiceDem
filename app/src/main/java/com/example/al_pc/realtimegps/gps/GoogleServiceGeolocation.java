package com.example.al_pc.realtimegps.gps;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.al_pc.realtimegps.gps.callback.IGPSCallback;
import com.example.al_pc.realtimegps.gps.listener.GoogleGPSListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class GoogleServiceGeolocation
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Context context;

    private GoogleApiClient googleApiClient;

    private IGPSCallback callback;

    private GoogleGPSListener listener;

    public GoogleServiceGeolocation(Context context){
        this.context = context;
    }

    @Override
    public void onConnected(Bundle bundle) {

        listener = new GoogleGPSListener(context, callback);

        if (googleApiClient.isConnected()) {

            int result = ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION");
            if (result != PackageManager.PERMISSION_DENIED) {
                PendingResult<Status> pendingResult = LocationServices
                        .FusedLocationApi
                        .requestLocationUpdates(googleApiClient, createLocationRequest(), listener);
            } else {
                Toast.makeText(context, "GoogleServiceGeolocation start failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startLocationListener(IGPSCallback callback) {
        this.callback = callback;
        startTracking();
    }

    public void stopLocationListener() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, listener);
    }

    private void startTracking() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            if (!googleApiClient.isConnected() || !googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        }
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
        Toast.makeText(context, "ConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, "ConnectionFailed", Toast.LENGTH_SHORT).show();
    }


}
