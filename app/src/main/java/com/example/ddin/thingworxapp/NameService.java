package com.example.ddin.thingworxapp;

import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vobis on 2017-04-14.
 */
public class NameService {

    private MainActivity parent;
    private ConcurrentHashMap appProperties;

    public NameService(MainActivity parent,ConcurrentHashMap appProperties){
        this.parent = parent;
        this.appProperties = appProperties;
    }

    //gets the device name
    public void deviceNameUpdate() {

        String deviceName = android.os.Build.MODEL;
        String deviceBrand = Build.BRAND;
        String device = deviceName.replaceAll("\\s+", "");
        String nameVal = deviceBrand + "." + device;
        appProperties.put("device", nameVal);
    }

    //gets the sim card IMEI
    public void imeiUpdate() {
        String imeiVal;
        TelephonyManager telephonyManager = (TelephonyManager) parent.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager.getDeviceId() == null) {
            imeiVal = Build.SERIAL;
        } else imeiVal = telephonyManager.getDeviceId();
        appProperties.put("phoneImei", imeiVal);
    }

    /*
    * Get phone Ip address
    */
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr =
                     intf.getInetAddresses(); enumIpAddr
                             .hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement
                            ();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }



}
