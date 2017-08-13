package com.gimbal.hello_gimbal_android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends ActionBarActivity {
    private GimbalEventReceiver gimbalEventReceiver;
    private GimbalEventListAdapter adapter;

    private static final String TAG = "SatvikAnand";



    /* DEFINE PROFIT*/
    public static String beacon = null;

    /* DEFINE MOVEMENT */
    public static String movement = null;

    /* DEFINE USER TYPE */
    public static final String type = "user";
    /* DEFINE USER ID */
    public static final String id = "1";
    /* DEFINE PROFIT*/
    public static final String profit = "10";
    public static final String serverAddress = "128.237.130.217";
    public static final int port = 25000;

    public static TextView myText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, AppService.class));


        adapter = new GimbalEventListAdapter(this);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        Log.i(TAG, "OnCreate");
        myText = (TextView)findViewById(R.id.myText);

        Button myButton = (Button)findViewById(R.id.myButton);
        myButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){


                        Client myClient = new Client(serverAddress, port, myText, beacon,movement,id,type,profit);
                        myClient.execute();




                        //myText.setText("Profit Sent!");
                    }
                }
        );

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setEvents(GimbalDAO.getEvents(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        gimbalEventReceiver = new GimbalEventReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GimbalDAO.GIMBAL_NEW_EVENT_ACTION);
        intentFilter.addAction(AppService.APPSERVICE_STARTED_ACTION);
        registerReceiver(gimbalEventReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(gimbalEventReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class GimbalEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().compareTo(GimbalDAO.GIMBAL_NEW_EVENT_ACTION) == 0) {
                    adapter.setEvents(GimbalDAO.getEvents(getApplicationContext()));
                }
            }
        }
    }


}
