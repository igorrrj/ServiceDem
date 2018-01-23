package com.example.al_pc.realtimegps.gps.callback;

import java.util.HashMap;

public interface IGPSCallback {
    void onLocationChanged(HashMap<String, Object> model, String provider);
}
