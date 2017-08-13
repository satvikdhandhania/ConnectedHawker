/**
 * Copyright (C) 2015 Gimbal, Inc. All rights reserved.
 *
 * This software is the confidential and proprietary information of Gimbal, Inc.
 *
 * The following sample code illustrates various aspects of the Gimbal SDK.
 *
 * The sample code herein is provided for your convenience, and has not been
 * tested or designed to work on any particular system configuration. It is
 * provided AS IS and your use of this sample code, whether as provided or
 * with any modification, is at your own risk. Neither Gimbal, Inc.
 * nor any affiliate takes any liability nor responsibility with respect
 * to the sample code, and disclaims all warranties, express and
 * implied, including without limitation warranties on merchantability,
 * fitness for a specified purpose, and against infringement.
 */
package com.gimbal.hello_gimbal_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GimbalDAO {
    public static final String GIMBAL_NEW_EVENT_ACTION = "GIMBAL_EVENT_ACTION";
    private static final String EVENTS_KEY = "events";

    public static List<String> getEvents(Context context) {
        List<String> events = new ArrayList<>();
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String jsonString = prefs.getString(EVENTS_KEY, null);
            if (jsonString != null) {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    events.add(jsonObject.getString("title"));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return events;
    }

    public static void setEvents(Context context, List<String> events) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            JSONArray jsonArray = new JSONArray();
            for (String event : events) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", event);
                jsonArray.put(jsonObject);
            }
            String jstr = jsonArray.toString();
            Editor editor = prefs.edit();
            editor.putString(EVENTS_KEY, jstr);
            editor.commit();

            // Notify activity
            Intent intent = new Intent();
            intent.setAction(GIMBAL_NEW_EVENT_ACTION);
            context.sendBroadcast(intent);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
