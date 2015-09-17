package com.example.rupertc.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    private TextView textView;
    public Switch switch1;
    public ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //textView = (TextView) findViewById(R.id.text1);
        //runUdpServer();
        new UDPServerTask().execute("");

        // Testing Switch for red wheel
        imageView = (ImageView) findViewById(R.id.imageView2);
        switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("UDPServer" , "Click!");
                //imageView.setImageResource(R.drawable.wheel2);
                if(imageView.getVisibility() == View.INVISIBLE) {
                    imageView.setVisibility(View.VISIBLE);
                }
                else
                {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    private static final int UDP_SERVER_PORT = 11111;
    private static final int MAX_UDP_DATAGRAM_LEN = 1500;
    public static String lText ="";

    /*** ASYNC TASK AGAINST EXCEPTION **/

    private class UDPServerTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            /*for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }*/
            //runUdpServer();  //THIS STARTS THE UDP SERVER

            byte[] lMsg = new byte[MAX_UDP_DATAGRAM_LEN];
            DatagramPacket dp = new DatagramPacket(lMsg, lMsg.length);
            DatagramSocket ds = null;
            try {
                ds = new DatagramSocket(UDP_SERVER_PORT);
                lText = "start";
                Log.e("UDPSERVER", "######################### SOCKET GENERATED!");
                while(!lText.contentEquals("stop"))
                {
                    ds.receive(dp);
                    lText = new String(lMsg, 0, dp.getLength());
                    Log.e("UDPSERVER", lText);
                    //textView.setText(lText);
                    //return lText;
                    publishProgress();
                }
            }
            catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (ds != null && lText.contentEquals("stop")) {
                    ds.close();
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            //TextView txt = (TextView) findViewById(R.id.output);
            //txt.setText("Executed"); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.e("UDPSERVER","onProgressUpdate: " + lText);
            Toast.makeText(getApplicationContext(),lText, Toast.LENGTH_SHORT).show();
            if(imageView.getVisibility() == View.INVISIBLE) {
                Log.e("UDPSERVER","Making the wheel visible!");
                imageView.setVisibility(View.VISIBLE);
            }
            else
            {
                Log.e("UDPSERVER","Making the wheel invisible!");
                imageView.setVisibility(View.INVISIBLE);
            }
        }
    }
    /*** END ***/
/*
    private void runUdpServer() {
        String lText;
        byte[] lMsg = new byte[MAX_UDP_DATAGRAM_LEN];
        DatagramPacket dp = new DatagramPacket(lMsg, lMsg.length);
        DatagramSocket ds = null;

        try {
            ds = new DatagramSocket(UDP_SERVER_PORT);
            Toast.makeText(getApplicationContext(), "Generated Socket!!!",
                    Toast.LENGTH_LONG).show();
            //disable timeout for testing
            //ds.setSoTimeout(100000);
            ds.receive(dp);
            lText = new String(lMsg, 0, dp.getLength());
            Log.i("UDP packet received", lText);
            textView.setText(lText);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ds != null) {
                ds.close();
            }
        }

    }
    */
}