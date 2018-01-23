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
import com.example.al_pc.realtimegps.service.LocationServiceNew;
import com.example.al_pc.realtimegps.util.PermissionUtil;

import io.reactivex.disposables.Disposable;

public class MainPresenter {

    private IMainActivity view;
    private Context context;
    private DBRepo db;
    private Disposable dbDisposable;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {

                Double lat = bundle.getDouble(LocationServiceNew.KEY_LAT);
                Double lng = bundle.getDouble(LocationServiceNew.KEY_LNG);

//                getDataSize();

                view.onLocationChanged(lat, lng).subscribe();
            }

        }

    };

    public MainPresenter(IMainActivity view, Activity context) {

        this.view = view;
        this.context = context;

        db = DBRepo.getInstance(context);

//        dbDisposable = getDataSize();
//
//        db.getLocationData()
//                .subscribe(
//                        (hashMaps, throwable) -> {}
//                );
//
//        db.getLocationDataSize()
//               .subscribe(
//                       (integer, throwable) -> {}
//               );

        //check permissions
        PermissionUtil.isLocationGranted(context);
        PermissionUtil.isNetworkGranted(context);

    }

    private Disposable getDataSize() {

       return db.getLocationDataSize()
                .subscribe(

                        size -> view.onLocationDataSizeChanged(size).subscribe(),
                        Throwable::printStackTrace

                );

    }

    public void subscribeLocationUpdates() {
        context.registerReceiver(broadcastReceiver, new IntentFilter(LocationServiceNew.BROADCAST_ACTION));
    }

    public void unsubscribeLocationUpdates() {

        context.unregisterReceiver(broadcastReceiver);

        if (dbDisposable != null && !dbDisposable.isDisposed())
            dbDisposable.dispose();

    }

}
