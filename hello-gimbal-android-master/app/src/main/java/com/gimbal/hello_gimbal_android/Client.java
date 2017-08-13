package com.gimbal.hello_gimbal_android;

/**
 * Created by Satvik on 4/26/17.
 */


import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends AsyncTask<Void, Void, String> {

    String dstAddress;
    int dstPort;
    String response = "";


    static String type;
    static String beaconReq;

    static String beacon;
    static String profit;
    static String movement;
    static String id;




    TextView textResponse;

    Client(String addr, int port, TextView textResponse, String beacon,String movement,String id, String type,String profit) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
        this.type = "type="+type;
        this.beacon = beacon;
        this.beaconReq = "beaconId="+beacon;
        this.profit = "profit="+profit;
        this.movement = "movement="+movement;
        this.id = "id="+id;

    }

    @Override
    protected String doInBackground(Void... arg0) {

        Socket socket = null;

        try {
            socket = new Socket(dstAddress, dstPort);


            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);


            String request = id+","+type+","+beaconReq+","+profit+","+movement;

            bw.write(request+"\n");
            bw.flush();



            System.out.println("Message sent to the server : "+request);


            System.out.println("Her1 : "+request);

            //Get the return message from the server
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            response = br.readLine();
            System.out.println("Message received from the server : " +response);


            String status = null ,hawkersInBeacon = null, hawkersInNearestZone = null;
            String splittedString[] = response.split(",");
            for( String s: splittedString){
                String keyVal[] = s.split("=");
                if(keyVal[0].equalsIgnoreCase("Status")){
                    status=keyVal[1];
                } else if(keyVal[0].equalsIgnoreCase("HawkersInBeacon")){
                    hawkersInBeacon=keyVal[1];
                } else if(keyVal[0].equalsIgnoreCase("HawkersInNearestZone")){
                    hawkersInNearestZone=keyVal[1];
                }
            }
            System.out.println("Her2 : "+request);
            if(status!=null){
                if(status.equalsIgnoreCase("Success")) {


                    if(hawkersInBeacon.equalsIgnoreCase("0") && hawkersInNearestZone!=null){
                        response = hawkersInNearestZone;
                    }

                    if(hawkersInNearestZone == null || !hawkersInBeacon.equalsIgnoreCase("0") ){
                        response  = hawkersInBeacon;

                    }
                }
                /*
                if(movement.contains("exit") && beacon.equals(MainActivity.beacon))
                    response = "0";
                    */
                }

            System.out.println("Her3 : "+request);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        textResponse.setText(response);
        super.onPostExecute(result);
    }

}