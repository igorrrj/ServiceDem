package com.example.al_pc.realtimegps.aplication.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.al_pc.realtimegps.R;
import com.example.al_pc.realtimegps.aplication.ui.main.MainPresenter;
import com.example.al_pc.realtimegps.aplication.ui.main.anstraction.IMainActivity;
import com.example.al_pc.realtimegps.service.LocationService;
import com.example.al_pc.realtimegps.util.PermissionUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity implements IMainActivity, OnMapReadyCallback {

    private static GoogleMap mGoogleMap;
    @BindView(R.id.start_track)
    Button start;
    @BindView(R.id.stop_track)
    Button stop;
    @BindView(R.id.location_data)
    TextView dataSize;
    @BindView(R.id.map)
    MapView mMapView;
    private MainPresenter presenter;

    private PolylineOptions polylineOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        start.setOnClickListener(v -> {

            startService(new Intent(MainActivity.this, LocationService.class));
            Toast.makeText(this, "startService", Toast.LENGTH_SHORT).show();

            if(mGoogleMap!=null){
                mGoogleMap.clear();
            }
            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.BLACK);
            polylineOptions.width(12);

        });

        stop.setOnClickListener(v -> {

            stopService(new Intent(MainActivity.this, LocationService.class));
            Toast.makeText(this, "stopService", Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

        presenter = new MainPresenter(this, this);

        presenter.subscribeLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();

        presenter.unsubscribeLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (!PermissionUtil.isLocationGranted(this)) {
            return;
        }
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public Completable onLocationChanged(double lat, double lng) {

        return Completable.fromAction(

                () -> {

                    Toast.makeText(this, "lat = " + lat + "\n lng = " + lng, Toast.LENGTH_SHORT).show();
                    LatLng latLng = new LatLng(lat, lng);
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));

                    polylineOptions.add(latLng);
                    mGoogleMap.addPolyline(polylineOptions);
                }

        ).subscribeOn(AndroidSchedulers.mainThread());

    }

    @SuppressLint("SetTextI18n")
    @Override
    public Completable onLocationDataSizeChanged(long size) {

        return Completable.fromAction(

                () -> dataSize.setText(getString(R.string.location_data_size) + " " + size)

        ).subscribeOn(AndroidSchedulers.mainThread());

    }


}
