package com.example.ddin.thingworxapp;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vobis on 2017-04-14.
 */
public class ViewGetter {

    private MainActivity parent;
    private HashMap<String, EditText> editTextsMap;
    private HashMap<String, ToggleButton> buttonsMap;

    private EditText ip;
    private EditText appKey;

    private ToggleButton on;

    public ViewGetter(MainActivity parent){
        this.parent = parent;
        editTextsMap = new HashMap<String,EditText>();
        buttonsMap = new HashMap<String, ToggleButton>();

        ip = (EditText) parent.findViewById(R.id.ip);
        appKey = (EditText) parent.findViewById(R.id.appKey);
        editTextsMap.put("ip",ip);
        editTextsMap.put("appKey", appKey);

        on = (ToggleButton) parent.findViewById(R.id.toggleButton);
        buttonsMap.put("on", on);
    }

    public String getEditText(String key){
        return editTextsMap.get(key).getText().toString();
    }

    public boolean getCheckedVal(String key){
        return buttonsMap.get(key).isChecked();
    }
}
