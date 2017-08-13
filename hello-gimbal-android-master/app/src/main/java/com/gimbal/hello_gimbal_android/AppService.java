package com.gimbal.hello_gimbal_android;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.gimbal.android.Communication;
import com.gimbal.android.CommunicationListener;
import com.gimbal.android.CommunicationManager;
import com.gimbal.android.Gimbal;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Push;
import com.gimbal.android.Visit;

import java.util.LinkedList;
import java.util.List;

import static com.gimbal.hello_gimbal_android.MainActivity.serverAddress;

public class AppService extends Service {

    public static final String APPSERVICE_STARTED_ACTION = "appservice_started";
    private static final int MAX_NUM_EVENTS = 2;

    private PlaceEventListener placeEventListener;
    private CommunicationListener communicationListener;
    private LinkedList<String> events;

    @Override
    public void onCreate(){
        events = new LinkedList<>(GimbalDAO.getEvents(getApplicationContext()));

        Gimbal.setApiKey(this.getApplication(), "b7360f6f-3b06-4bea-9dc2-457068a1f425");
        setupGimbalPlaceManager();
        setupGimbalCommunicationManager();
        Gimbal.start();
    }

    private void setupGimbalCommunicationManager() {
        communicationListener = new CommunicationListener() {
            @Override
            public Notification.Builder prepareCommunicationForDisplay(Communication communication, Visit visit, int notificationId) {
                addEvent(String.format( "%s", communication.getTitle()));


                // If you want a custom notification create and return it here
                return null;
            }

            @Override
            public Notification.Builder prepareCommunicationForDisplay(Communication communication, Push push, int notificationId) {
                addEvent(String.format( "Push Communication Delivered : %s", communication.getTitle()));
                // If you want a custom notification create and return it here
                return null;
            }

            @Override
            public void onNotificationClicked(List<Communication> communications) {
                for (Communication communication : communications) {
                    if(communication != null) {
                        addEvent("Communication Clicked");
                    }
                }
            }
        };
        CommunicationManager.getInstance().addListener(communicationListener);
    }

    private void setupGimbalPlaceManager() {
        placeEventListener = new PlaceEventListener() {

            @Override
            public void onVisitStart(Visit visit) {
                addEvent(String.format("Start Visit for %s", visit.getPlace().getName()));
                MainActivity.beacon = visit.getPlace().getName();
                MainActivity.movement = "entry";



                Client myClient = new Client(serverAddress, MainActivity.port, MainActivity.myText, MainActivity.beacon,MainActivity.movement,MainActivity.id,MainActivity.type,MainActivity.profit);
                myClient.execute();

                Toast.makeText(getApplicationContext(), "Sent Profits.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVisitEnd(Visit visit) {
                addEvent(String.format("End Visit for %s", visit.getPlace().getName()));

                String beacon = visit.getPlace().getName();
                String movement = "exit";


                Client myClient = new Client(serverAddress, MainActivity.port, MainActivity.myText, beacon,movement,MainActivity.id,MainActivity.type,MainActivity.profit);
                myClient.execute();

                if(beacon.equalsIgnoreCase(MainActivity.beacon)){
                    MainActivity.beacon = null;
                    MainActivity.movement = null;
                }

            }
        };
        PlaceManager.getInstance().addListener(placeEventListener);
    }

    private void addEvent(String event) {
        while (events.size() >= MAX_NUM_EVENTS) {
            events.removeLast();
        }
        events.add(0, event);
        GimbalDAO.setEvents(getApplicationContext(), events);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        notifyServiceStarted();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        PlaceManager.getInstance().removeListener(placeEventListener);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void notifyServiceStarted() {
        Intent intent = new Intent(APPSERVICE_STARTED_ACTION);
        sendBroadcast(intent);
    }
}
