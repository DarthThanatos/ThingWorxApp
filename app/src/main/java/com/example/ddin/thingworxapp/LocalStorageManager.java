package com.example.ddin.thingworxapp;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vobis on 2017-04-14.
 */
public class LocalStorageManager {

    private MainActivity parent;
    private ConcurrentHashMap appProperties;

    public LocalStorageManager(MainActivity parent, ConcurrentHashMap appProperties){
        this.parent = parent;
        this.appProperties = appProperties;
        setHardCodedStorage();
    }
    
    public void writeToLocalStorage() {
        File file = parent.getFileStreamPath("localStorageFile.txt");
        String ip = parent.viewGetter.getEditText("ip");
        String appKey = parent.viewGetter.getEditText("appKey");
        if (!file.exists()) {
            try {
                file.createNewFile();

                String string = ip + "," + appKey;
                FileOutputStream writer = parent.openFileOutput(file.getName(), Context.MODE_PRIVATE);
                writer.write(string.getBytes());
                writer.flush();
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
                //    makeText(this, "Exception: " + e.getMessage(), LENGTH_LONG).show();
                System.err.println(" there was an error in creating the file " + e.getMessage());
            }
        } else {
            try {

                String string = ip + "," + appKey;
                FileOutputStream writer = parent.openFileOutput(file.getName(), Context.MODE_PRIVATE);
                writer.write(string.getBytes());
                writer.flush();
                writer.close();

            } catch (Exception ex) {
                System.err.println("there was an error in writing " + ex.getMessage());
            }
        }
    }

    public String readFromInternalStorage() throws IOException {
        String temp = "";
        try {
            InputStream in = parent.openFileInput("localStorageFile.txt");
            if (in != null) {
                InputStreamReader tmp = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(tmp);
                String str;
                StringBuffer buf = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buf.append(str + "\n");
                }
                in.close();
                temp = "Not Working";
                temp = buf.toString();
                //    makeText(this, temp, LENGTH_SHORT).show();
            }
        } catch (java.io.FileNotFoundException e) {
            // that's OK, we probably haven't created it yet
            System.err.print("file not found ");
        } catch (Throwable t) {
            //  makeText(this, "Exception: " + t.toString(), LENGTH_LONG).show();
            System.err.print("an error occured " + t.getMessage());
        }
        return temp;
    }

    public void setHardCodedStorage(){
        String ipValue = "54.72.160.77";
        String appKeyValue = "702fa820-093d-4a56-a46e-5b848b4cdab2";
        String thing = "359043054109845";//"357932008744396";
        appProperties.put("ip",ipValue);
        appProperties.put("appKey", appKeyValue);
        appProperties.put("phoneImei", thing);
        System.out.println("\n\n\nset hardcoded: ip" + ipValue + " appkey" + appKeyValue + "\n\n\n");
    }

    public void setStorageVal(){
        String ipValue = "";
        String appKeyValue = "";
        try {
            String localStorageData = readFromInternalStorage();
            if (!localStorageData.isEmpty()) {
                ipValue = localStorageData.split(",")[0].trim();
                if (!localStorageData.split(",")[1].isEmpty()) {
                    appKeyValue = localStorageData.split(",")[1].trim();
                    appProperties.put("ip",ipValue);
                    appProperties.put("appKey", appKeyValue);
                    System.out.println("\n\n\nset: ip" + ipValue + " appkey" + appKeyValue + "\n\n\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

}
