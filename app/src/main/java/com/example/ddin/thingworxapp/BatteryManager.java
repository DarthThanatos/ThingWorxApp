package com.example.ddin.thingworxapp;

import android.app.Notification;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vobis on 2017-04-14.
 */
public class BatteryManager {

    private MainActivity parent;
    private ConcurrentHashMap appProperties;

    public BatteryManager(MainActivity parent, ConcurrentHashMap appProperties){
        this.parent = parent;
        this.appProperties = appProperties;
    }

    public void  batteryLevelUpdate(){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = parent.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(android.os.BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(android.os.BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float)scale * 100;
        appProperties.put("batteryStatus",Double.toString(batteryPct));
        System.out.println("Battery lvl: " + level + " scale: " + scale);
    }
}
