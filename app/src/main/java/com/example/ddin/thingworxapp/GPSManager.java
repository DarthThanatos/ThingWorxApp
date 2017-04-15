package com.example.ddin.thingworxapp;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vobis on 2017-04-14.
 */
public class GPSManager {

    private LocationManager locationManager;
    private String provider;
    private ConcurrentHashMap appProperties;

    public GPSManager(MainActivity parent, ConcurrentHashMap appProperties){
        this.appProperties = appProperties;

        locationManager = (LocationManager) parent.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            updateLocation(location);
        } else {
            appProperties.put("location", "0:0");
        }

    }

    public void updateLocation(Location location){
        double lat = (double) (location.getLatitude());
        double lng = (double) (location.getLongitude());
        double alt = (double) location.getAltitude();
        appProperties.put("location", Double.toString(lat) + ":" +  Double.toString(lng) + ":" + Double.toString(alt));
        appProperties.put("latitude", Double.toString(lat));
        appProperties.put("longitude", Double.toString(lng));
    }
}
