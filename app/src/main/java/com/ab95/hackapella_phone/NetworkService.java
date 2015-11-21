package com.ab95.hackapella_phone;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class NetworkService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkRunnable networkRunnable = new NetworkRunnable(this);
        Thread networkThread = new Thread(networkRunnable);
        networkThread.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


