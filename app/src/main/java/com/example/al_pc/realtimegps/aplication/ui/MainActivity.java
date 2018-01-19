package com.example.al_pc.realtimegps.aplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.example.al_pc.realtimegps.R;
import com.example.al_pc.realtimegps.aplication.ui.main.MainPresenter;
import com.example.al_pc.realtimegps.aplication.ui.main.anstraction.IMainActivity;
import com.example.al_pc.realtimegps.service.LocationService;

public class MainActivity extends AppCompatActivity implements IMainActivity{

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this, this);

        Button start = findViewById(R.id.start_track);
        start.setOnClickListener(v -> startService(new Intent(MainActivity.this, LocationService.class)));

        Button stop = findViewById(R.id.stop_track);
        stop.setOnClickListener(v -> stopService(new Intent(MainActivity.this, LocationService.class)));

    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.subscribeLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.unsubscribeLocationUpdates();
    }


    @Override
    public void onLocationChanged(double lat, double lng) {
        Toast.makeText(this, "lat = " + lat + "\n lng = "+ lng, Toast.LENGTH_SHORT).show();
    }
}
