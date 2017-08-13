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

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GimbalEventListAdapter extends BaseAdapter {
    private Activity activity;
    private List<String> events = new ArrayList<>();


    public GimbalEventListAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setEvents(List<String> events) {
        this.events.clear();
        this.events.addAll(events);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String event = events.get(position);
        View view = convertView;
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.list_item, null);
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(event);


        return view;
    }



}
