package com.example.al_pc.realtimegps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.al_pc.realtimegps.service.LocationService;

public class MainActivity extends AppCompatActivity {

    private Button start, stop;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                Double lat = bundle.getDouble(LocationService.KEY_LAT);
                Double lng = bundle.getDouble(LocationService.KEY_LNG);

                Toast.makeText(getApplicationContext(), "lat = " + lat + "\n lng = "+ lng, Toast.LENGTH_SHORT).show();

            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtil.isLocationGranted(this);
        PermissionUtil.isNetworkGranted(this);

        start = findViewById(R.id.start_track);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startService(new Intent(MainActivity.this, LocationService.class));

            }
        });

        stop = findViewById(R.id.stop_track);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopService(new Intent(MainActivity.this, LocationService.class));

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(LocationService.BROADCAST_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }


}
