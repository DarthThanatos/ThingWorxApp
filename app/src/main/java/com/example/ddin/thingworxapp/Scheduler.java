package com.example.ddin.thingworxapp;

import android.hardware.SensorEvent;
import android.location.Location;
import android.os.AsyncTask;
import android.view.View;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


public class Scheduler {

    private MainActivity parent = null;
    private ConcurrentHashMap appProperties;

    private SensorSystem sensorSystem;
    private LocalStorageManager localStorageManager;
    private CPUManager cpuManager;
    private MemoryManager memoryManager;
    private NameService nameService;
    private GPSManager gpsManager;
    private BatteryManager batteryManager;
    private Sender sender;

    private static volatile boolean shouldSend = true;

    private Timer timer;

    public Scheduler(MainActivity parent, ConcurrentHashMap appProperties) {
        this.parent = parent;
        this.appProperties = appProperties;

        sensorSystem = new SensorSystem(parent, appProperties);
        localStorageManager = new LocalStorageManager(parent, appProperties);
        cpuManager = new CPUManager(appProperties);
        memoryManager = new MemoryManager(parent,appProperties);
        nameService = new NameService(parent,appProperties);
        gpsManager = new GPSManager(parent,appProperties);
        sender = new Sender(parent, appProperties);
        batteryManager = new BatteryManager(parent, appProperties);
        parent.runOnUiThread(new ViewSetter(parent, appProperties));

        timer = new Timer();

    }

    String ip;
    String thing;
    String appKey;

    private void setURLParts(){
        ip = parent.viewGetter.getEditText("ip");
        appKey = parent.viewGetter.getEditText("appKey");
        appProperties.put("ip",ip);
        appProperties.put("appKey", appKey);
        thing = (String) appProperties.get("phoneImei");
    }

    private long lastSensorUpdate = 0;

    public void scheduleSensorUpdate(SensorEvent sensorEvent){
        long curTime = System.currentTimeMillis();
        long diffTime = curTime - lastSensorUpdate;
        if(diffTime > 5000){
            lastSensorUpdate = curTime;
            boolean sensorChangeAcknowledged = sensorSystem.processSensor(sensorEvent, diffTime);

            if(sensorChangeAcknowledged) {
                setURLParts();
                String shakeTreshold = (String) appProperties.get("ShakeTreshold");
                new MyTask().execute(ip, appKey, thing, shakeTreshold);
            }
        }
    }

    public void pauseTimer() {
        timer.cancel();
        localStorageManager.writeToLocalStorage();

        try {
            setURLParts();

            new MyTask().execute(ip, appKey, thing, "-1");
            Thread.sleep(3000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void scheduleLocationUpdate(Location location){
        gpsManager.updateLocation(location);
    }

    public void resumeTimer(TimerTask aTask) {
        timer = new Timer();
        timer.scheduleAtFixedRate(aTask, 0, 3000); // no delay, executed every 3 seconds
    }

    public void manageTimer(View view){
        final boolean on = parent.viewGetter.getCheckedVal("on");

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                setURLParts();
                try {
                    cpuManager.usageUpdate();
                    memoryManager.availRamsUpdate();
                    memoryManager.totalRamsUpdate();
                    nameService.deviceNameUpdate();
                    nameService.imeiUpdate();
                    batteryManager.batteryLevelUpdate();

                    Map<String, String> map = new HashMap<>();
                    map.put("cpuUsage", (String) appProperties.get("cpuUsage"));
                    map.put("totalRams", (String)appProperties.get("totalRams"));
                    map.put("availRams", (String)appProperties.get("availRams"));
                    map.put("device", (String) appProperties.get("device"));
                    map.put("phoneImei", String.valueOf(appProperties.get("phoneImei")));
                    map.put("location", String.valueOf(appProperties.get("location")));
                    map.put("batteryStatus", (String) appProperties.get("batteryStatus"));
                    sender.sendPropertyValue(ip, appKey, thing, map);
                    localStorageManager.writeToLocalStorage();

                } catch (Exception e) {

                    System.err.println("Exception caught 1," + e.getMessage() + ip);
                    System.out.println(" connection was not established " + e.getMessage());
                    String connectionMessageText = "Connection error, check IP,  Thing Name or App Key";
                    appProperties.put("connectionMessageText", connectionMessageText);
                    timer.cancel();
                }
                finally {
                    parent.runOnUiThread(new ViewSetter(parent, appProperties));
                }
            }
        };
        if(on){
            resumeTimer(timerTask);
        }
        else{
            appProperties.put("connectionMessageText", "No data is currently sent to TWX");
            parent.runOnUiThread(new ViewSetter(parent, appProperties));
            pauseTimer();
        }
    }

    public void refreshSending(){
        shouldSend = true;
    }

    public void stopRefreshing(){
        shouldSend = false;
    }

    private class MyTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                sender.sendPropertyValues(params[0], params[1], params[2], "ShakeTreshold", params[3]);
                //System.err.println("sending shakeTreshold data "+params[3]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}

