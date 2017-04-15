package com.example.ddin.thingworxapp;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vobis on 2017-04-14.
 */
public class MemoryManager {

    private ConcurrentHashMap appProperties;
    MainActivity parent;

    public MemoryManager(MainActivity parent, ConcurrentHashMap appProperties){
        this.parent = parent;
        this.appProperties = appProperties;
    }

    // gets the Total RAM
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN) //to indicate that target api version is 16
    public void totalRamsUpdate() throws Exception {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) parent.getSystemService(ActionBarActivity.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        String totalRamsVal = Double.toString(mi.availMem / 1048576L);
        appProperties.put("totalRams", totalRamsVal);
    }

    // gets the Free RAM
    public void availRamsUpdate() throws Exception {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) parent.getSystemService(ActionBarActivity.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        String totalRamsVal = Double.toString(mi.availMem / 1048576L);
        appProperties.put("availRams", totalRamsVal);
    }
}
