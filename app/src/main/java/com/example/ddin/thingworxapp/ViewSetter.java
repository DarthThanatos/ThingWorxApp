package com.example.ddin.thingworxapp;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DecimalFormat;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vobis on 2017-04-14.
 */
public class ViewSetter extends TimerTask{

    private MainActivity parent;
    private ConcurrentHashMap appProperties;

    private TextView batteryStatus;
    private TextView imei;
    private TextView device;
    private TextView cpuUsage;
    private TextView latitudeField;
    private TextView longitudeField;
    private TextView availRam;
    private TextView totalRam;
    private Button gps;
    private TextView shakeTresholdTV;
    private ToggleButton toggleButton;
    private TextView connectionMessage;
    private EditText ip;
    private EditText appKey;

    private String[] propertiesNames = {"cpuUsage", "totalRams", "availRams", "device", "phoneImei", "location", "ShakeTreshold", "connectionMessageText"};

    public ViewSetter(MainActivity parent, ConcurrentHashMap appProperties) {
        this.parent = parent;
        this.appProperties = appProperties;
        availRam = (TextView) parent.findViewById(R.id.availRam);
        totalRam = (TextView) parent.findViewById(R.id.totalRam);
        cpuUsage = (TextView) parent.findViewById(R.id.cpuUsage);
        latitudeField = (TextView) parent.findViewById(R.id.latitudeField);
        longitudeField = (TextView) parent.findViewById(R.id.longitudeField);
        batteryStatus = (TextView) parent.findViewById(R.id.batteryStatus);
        imei = (TextView) parent.findViewById(R.id.imei);
        device = (TextView) parent.findViewById(R.id.device);
        shakeTresholdTV = (TextView) parent.findViewById(R.id.shakeTresholdView);
        connectionMessage = (TextView) parent.findViewById(R.id.connectionMessage);
        toggleButton = (ToggleButton) parent.findViewById(R.id.toggleButton);
        ip = (EditText) parent.findViewById(R.id.ip);
        appKey = (EditText) parent.findViewById(R.id.appKey);
    }

    private void setMemoryView(){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        String availRamVal = (String)appProperties.get("availRams");
        String totalRamVal = (String)appProperties.get("totalRams");
        availRam.setText(df.format(Double.parseDouble(availRamVal)));
        totalRam.setText(df.format(Double.parseDouble(totalRamVal)));
    }


    private void setCpuUsageView(){
        String cpuLoad = (String) appProperties.get("cpuUsage");
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        System.out.println("CPU LOAD: " + cpuLoad);
        cpuUsage.setText(String.valueOf(df.format(Double.parseDouble(cpuLoad))) + "%");
    }

    private void setLocationView(){
        String latVal = (String) appProperties.get("latitude");
        String lngVal = (String) appProperties.get("longitude");
        latitudeField.setText(String.valueOf(latVal));
        longitudeField.setText(String.valueOf(lngVal));
    }

    private void setBatteryView(){
        String batteryStateVal = (String)appProperties.get("batteryStatus");
        batteryStatus.setText(batteryStateVal + "%");
    }

    private void setNameView(){
        String deviceVal = (String) appProperties.get("device");
        String imeiVal = (String) appProperties.get("phoneImei");
        String ipVal = (String) appProperties.get("ip");
        String appKeyVal = (String) appProperties.get("appKey");
        imei.setText(imeiVal);
        device.setText(deviceVal);
        ip.setText(ipVal);
        appKey.setText(appKeyVal);
    }

    private void setShakeHandView(){
        String shakeTresholdVal = (String) appProperties.get("ShakeTreshold");
        shakeTresholdTV.setText(shakeTresholdVal);
    }

    private void setConnectionMessageView(){
        String connectionMessageTextVal = (String) appProperties.get("connectionMessageText");
        connectionMessage.setText(connectionMessageTextVal);
    }

    @Override
    public void run() {
        System.out.println("View setter");
        setConnectionMessageView();
        setCpuUsageView();
        setLocationView();
        setMemoryView();
        setNameView();
        setShakeHandView();
        setBatteryView();
    }
}
