package com.delex.utility;

/*
 * Created by moda on 12/04/16.
 */


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


/*
*
* To ping to google server every 5 seconds, incase the user is signed in and socket is not connected and if response comes than it tries to connect the socket by sending the callback to the Appcontroller application class
*
* */
public class ConnectionService extends Service {


    public static String TAG_INTERVAL = "interval";
    public static String TAG_URL_PING = "url_ping";
    public static String TAG_ACTIVITY_NAME = "activity_name";


    private String url_ping;


    private Timer mTimer = null;

    ConnectionServiceCallback mConnectionServiceCallback;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public interface ConnectionServiceCallback {
        void hasInternetConnection();

        void hasNoInternetConnection();
    }


    @SuppressWarnings("TryWithIdenticalCatches")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {

            int interval = intent.getIntExtra(TAG_INTERVAL, 5);
            url_ping = intent.getStringExtra(TAG_URL_PING);
            String activity_name = intent.getStringExtra(TAG_ACTIVITY_NAME);

            try {
                mConnectionServiceCallback = (ConnectionServiceCallback) Class.forName(activity_name).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new CheckForConnection(), 0, interval * 1000);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private class CheckForConnection extends TimerTask {
        @Override
        public void run() {
            isNetworkAvailable();
        }
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    @SuppressWarnings("TryWithIdenticalCatches")

    private boolean isNetworkAvailable() {


        URL url = null;
        try {
            url = new URL(url_ping);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();


            urlc.setConnectTimeout(5000);
            urlc.connect();


            if (urlc.getResponseCode() == 200) {


                if (mConnectionServiceCallback != null) {

                    mConnectionServiceCallback.hasInternetConnection();

                }


                return true;

            } else {


                if (mConnectionServiceCallback != null) {
                    mConnectionServiceCallback.hasNoInternetConnection();
                }
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

}