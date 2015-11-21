package com.ab95.hackapella_phone;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkRunnable implements Runnable {

    LocationManager locationManager;
    LocationListener locationListener;
    Location location;

    ServerSocket serverSocket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    public NetworkRunnable(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location newLocation) {
                location = newLocation;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    @Override
    public void run() {
        Looper.prepare();
        while (true) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                serverSocket = new ServerSocket(7000);
                Socket socket = serverSocket.accept();
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                send(locationToString(location));
                socket.close();
                serverSocket.close();
            }
            catch (IOException e) {
                Log.e("NetworkService", e.getMessage());
            }
        }
    }

    private void send(String string) {
        try {
            dataOutputStream.write(string.getBytes());
            Log.i("Network", "sent");
        }
        catch (IOException | NullPointerException e) {
            Log.e("NetworkClient", e.getMessage());
        }
    }

    private String locationToString(Location location) {
        String string = "";
        string += Double.toString(location.getLatitude());
        string += ",";
        string += Double.toString(location.getLongitude());
        string += ",";
        string += Long.toString(location.getTime());
        string += ",";
        return string;
    }
}
