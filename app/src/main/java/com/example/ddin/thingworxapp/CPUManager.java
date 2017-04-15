package com.example.ddin.thingworxapp;

import android.app.Notification;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vobis on 2017-04-14.
 */
public class CPUManager {

    private ConcurrentHashMap appProperties;

    public CPUManager(ConcurrentHashMap appProperties){
        this.appProperties = appProperties;
    }

    // gets the CPU Load
    public void usageUpdate() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();

            String[] toks = load.split(" +");  // Split on one or more spaces

            long idle1 = Long.parseLong(toks[4]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {
            }

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" +");

            long idle2 = Long.parseLong(toks[4]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
            String cpuUsageVal = Double.toString(((float) (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1))) * 100);
            appProperties.put("cpuUsage", cpuUsageVal);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
