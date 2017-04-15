package com.example.ddin.thingworxapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;


public class MainActivity extends Activity implements LocationListener, SensorEventListener {
    public final static String DEBUG_TAG = "MakePhotoActivity";
    private ConcurrentHashMap<String,String> appProperties;
    private String[] propertiesNames = {"cpuUsage", "totalRams", "availRams", "device", "phoneImei", "location", "ShakeTreshold", "ip", "appKey", "connectionMessageText", "longitude", "latitude", "batteryStatus"};
    private String[] propertiesDefaultVals = {"0.0", "0.0", "0.0", "Device", "359043054109845", "0:0", "0.5", "54.72.160.77", "702fa820-093d-4a56-a46e-5b848b4cdab2", "", "0","0", "100"};

    public ViewGetter viewGetter;
    public ViewSetter viewSetter;

    Scheduler scheduler;

    private int cameraId = 0;
    private Camera camera;


    private int findBackFacingCamera() {
        int cameraId = -1;
        // Search for the back facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Log.d(DEBUG_TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appProperties = new ConcurrentHashMap<String,String>();
        for (int i = 0; i < propertiesNames.length; i++){
            appProperties.put(propertiesNames[i],propertiesDefaultVals[i]);
        }
        viewGetter = new ViewGetter(this);
        viewSetter = new ViewSetter(this, appProperties);

        scheduler = new Scheduler(this, appProperties);

        camera = Camera.open();
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        if(camera != null){
            CameraPreview mCameraPreview = new CameraPreview(this, camera);
            preview.addView(mCameraPreview);
        }


    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    };

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    public void onPhotoClick(View view){
        try {
            if(camera != null) {
                System.out.println("Sending photo");
                camera.takePicture(null, null, new PhotoHandler(this));
            }
        }catch(IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        scheduler.scheduleLocationUpdate(location);
    }

    //refresh every 0.5 seconds the button that gets phone data on display
    public void onRefreshClick(View view) throws Exception {
        scheduler.refreshSending();
    }

    // stop refreshing on display
    public void onStopClick(View view) throws Exception {
        scheduler.stopRefreshing();
    }

    public void onClicks(View view) {
       scheduler.manageTimer(view);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        scheduler.scheduleSensorUpdate(sensorEvent);
    }

    public void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.release();
            camera = null;
        }
        Toast.makeText(getApplicationContext(), "Thanks for using application!!", Toast.LENGTH_LONG).show();
        System.err.println("Back was pressed");
        scheduler.pauseTimer();
        finish();
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
