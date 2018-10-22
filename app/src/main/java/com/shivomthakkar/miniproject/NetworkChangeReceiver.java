package com.shivomthakkar.miniproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Shivom on 10/23/17.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static final String TAG = "NetworkChangeReceiver";
    SharedPreferences prefs;

    String networkSSID = "\"Wifi\"";

    @Override
    public void onReceive(Context context, Intent intent) {

        prefs = context.getSharedPreferences("AttendanceLogger", Context.MODE_PRIVATE);
        networkSSID = prefs.getString("ConnectTo", "");
        scanForChanges(context);

    }

    void scanForChanges(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {

            boolean isNotificationRequired = false;

            WifiConfiguration wifiConfiguration = new WifiConfiguration();
            wifiConfiguration.SSID = prefs.getString("ConnectTo", "");
            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if(!connectionInfo.getSSID().equals(prefs.getString("ConnectTo", ""))) {
                isNotificationRequired = true;
            }
            int netId = wifiManager.addNetwork(wifiConfiguration);

            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();

            notifyOnConnect(context, isNotificationRequired);
            //context.startService(new Intent(context, StudentService.class));
        }
    }

    void notifyOnConnect(final Context context, final boolean isNotificationRequired) {
        Log.i(TAG, "Connected");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && !(connectionInfo.getSSID().equals(""))) {
                    Log.i(TAG, connectionInfo.getSSID());
                    String ssid = connectionInfo.getSSID();
                    Log.i(TAG, "Current connected SSID: " + ssid);
                    Log.i(TAG, "Searching for SSID: " + networkSSID);
                    if (ssid.equals(networkSSID) && isNotificationRequired) {
                        Intent notificationIntent = new Intent(context, LoginActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), notificationIntent, 0);
                        Notification notification = new Notification.Builder(context)
                                .setContentTitle("Your chance to log in your attendance!")
                                .setAutoCancel(true)
                                .setContentText("Quick! Hurry up!")
                                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon))
                                .setSmallIcon(android.R.drawable.btn_plus).setContentIntent(pendingIntent)
                                .addAction(R.drawable.wifi_img, "Log In", pendingIntent)
                                .build();

                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(0, notification);

                        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        Log.i(TAG, "Vibrating now.");
                        vibrator.vibrate(300);
                    }
                }
            }
        }, 4500);

    }
}
