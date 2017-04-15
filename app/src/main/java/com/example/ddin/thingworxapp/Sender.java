package com.example.ddin.thingworxapp;

import android.support.v7.app.ActionBarActivity;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vobis on 2017-04-14.
 */
public class Sender {

    private MainActivity parent;
    private ConcurrentHashMap appProperties;

    public Sender(MainActivity parent, ConcurrentHashMap appProperties){
        this.parent = parent;
        this.appProperties = appProperties;
    }

    // opens the REST request
    public void sendPropertyValues(String ipAddress, String appKey, String thing, String property, String value) throws Exception {
        String connectionMessageText;
        URL url = new URL("http://" + ipAddress
                + "/Thingworx"
                + "/Things/AndroidDevice_" + thing
                + "/Properties/" + property
                + "?method=put"
                + "&value=" + value
                + "&appKey=" + appKey);
        //System.out.println("sendPropertyValues  " + url);
        HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.connect();
            //System.out.println(" connection was established ? " + conn.getResponseMessage());
            //System.out.println(" connection response code: " + conn.getResponseCode());
            if(conn.getResponseCode()==500){
                System.out.println(" There was an internal server error " + conn.getResponseMessage());
            }
            if (conn.getResponseCode() != 200 && conn.getResponseCode()!=500) {
                connectionMessageText = "Connection error";
                System.err.println("\nFailed : HTTP error code : " + conn.getResponseCode() + " " + "http://" + ipAddress
                        + "/Thingworx"
                        + "/Things/AndroidDevice_" + thing
                        + "/Properties/" + property
                        + "?method=put"
                        + "&value=" + value
                        + "&appKey=" + appKey);
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());

            }
            else {
                connectionMessageText = "Sending data to TWX successfully";
            }
            conn.disconnect();
            if(conn.getResponseCode()==500){
                System.out.println(" There was an internal server error " + conn.getResponseMessage());
            }
            if (conn.getResponseCode() != 200 && conn.getResponseCode() != 500) {
                connectionMessageText = "Connection error, check IP, Thing Name or App Key";
                //      MainActivity.this.runOnUiThread(setConnectionMessage);
                System.err.println("Failed : HTTP error code : " + conn.getResponseCode());
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            else {
                connectionMessageText = "Sending data to TWX successfully";
            }

            conn.disconnect();
            appProperties.put("connectionMessageText", connectionMessageText);
    }

    // sends the values via REST
    public void sendPropertyValue(String ipAddress, String appKeys, String thing, Map<String, String> entrySet) throws Exception {

        for (String key : entrySet.keySet())
            sendPropertyValues(ipAddress, appKeys, thing, key, entrySet.get(key));
    }
}
