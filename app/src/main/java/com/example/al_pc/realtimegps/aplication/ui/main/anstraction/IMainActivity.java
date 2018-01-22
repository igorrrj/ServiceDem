package com.example.al_pc.realtimegps.aplication.ui.main.anstraction;

import io.reactivex.Completable;

public interface IMainActivity {

    Completable onLocationChanged(double lat, double lng);
    Completable onLocationDataSizeChanged(long size);

}
