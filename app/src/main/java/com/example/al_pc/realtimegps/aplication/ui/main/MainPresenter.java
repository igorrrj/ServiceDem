package com.example.al_pc.realtimegps.aplication.ui.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.al_pc.realtimegps.aplication.data.local.db.DBRepo;
import com.example.al_pc.realtimegps.aplication.ui.main.anstraction.IMainActivity;
import com.example.al_pc.realtimegps.service.LocationService;
import com.example.al_pc.realtimegps.util.PermissionUtil;

public class MainPresenter {

    private IMainActivity view;
    private Context context;
    private DBRepo db;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {

                Double lat = bundle.getDouble(LocationService.KEY_LAT);
                Double lng = bundle.getDouble(LocationService.KEY_LNG);

                view.onLocationChanged(lat, lng);
            }

        }

    };

    public MainPresenter(IMainActivity view, Activity context) {
        this.view = view;
        this.context = context;

        //check permissions
        PermissionUtil.isLocationGranted(context);
        PermissionUtil.isNetworkGranted(context);
    }

    public void subscribeLocationUpdates() {
        context.registerReceiver(broadcastReceiver, new IntentFilter(LocationService.BROADCAST_ACTION));
    }

    public void unsubscribeLocationUpdates() {
        context.unregisterReceiver(broadcastReceiver);
    }

}
