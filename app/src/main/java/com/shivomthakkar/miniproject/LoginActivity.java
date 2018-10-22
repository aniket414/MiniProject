package com.shivomthakkar.miniproject;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_IMEI_PERMISSION = 100;
    AutoCompleteTextView eTName, eTYear, eTRollNo, eTDivision;
    Button btnSubmit, btnIsTeacher;
    SharedPreferences prefs;
    private String TAG = "LoginActivity";
    private String[] permissions = {Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getIds();


        String name = prefs.getString("Name", "");
        int rollNo = prefs.getInt("Roll No", 9999);
        String studentYear = prefs.getString("Year", "");
        String division = prefs.getString("Division", "");
        String IMEI = prefs.getString("IMEI", "");
        boolean isTeacher = prefs.getBoolean("isTeacher", false);

        if (!name.equals("") && rollNo != 9999 && !studentYear.equals("") && !division.equals("") && !IMEI.equals("")) {
            if(!isTeacher) {

                Intent broadcastIntent = new Intent("com.shivomthakkar.miniproject.NetworkChangeReceiver");
                sendBroadcast(broadcastIntent);

                connectToWifi();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }/* else {
                Intent intent = new Intent(this, OptionsActivity.class);
                startActivity(intent);
                finish();
            }*/
        }
    }

    void getIds() {
        eTName = (AutoCompleteTextView) findViewById(R.id.eTName);
        eTRollNo = (AutoCompleteTextView) findViewById(R.id.eTRollNo);
        eTDivision = (AutoCompleteTextView) findViewById(R.id.eTDivision);
        eTYear = (AutoCompleteTextView) findViewById(R.id.eTYear);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnIsTeacher = (Button) findViewById(R.id.btnIsTeacher);
        btnIsTeacher.setOnClickListener(this);

        prefs = getSharedPreferences("AttendanceLogger", Context.MODE_PRIVATE);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSubmit:

                if (eTName.getText().toString().equals("")) {
                    eTName.requestFocus();
                } else if (eTRollNo.getText().toString().equals("")) {
                    eTRollNo.requestFocus();
                } else if (eTYear.getText().toString().equals("")) {
                    eTYear.requestFocus();
                } else if (eTDivision.getText().toString().equals("")) {
                    eTDivision.requestFocus();
                } else {
                    getIMEI();
                }
                break;
            case R.id.btnIsTeacher:
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isTeacher", true);
                editor.apply();
                Intent intent = new Intent(this, OptionsActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }


    @TargetApi(23)
    void getIMEI() {
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

            String imei = tm.getDeviceId(1);

            Log.i(TAG, "IMEI: " + imei);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Name", eTName.getText().toString());
            editor.putInt("Roll No", Integer.parseInt(eTRollNo.getText().toString()));
            editor.putString("Year", eTYear.getText().toString());
            editor.putString("Division", eTDivision.getText().toString());
            editor.putString("IMEI", imei);
            editor.putString("ConnectTo", "\"Teacher" + eTYear.getText().toString() + eTDivision.getText().toString() + "\"");
            editor.apply();

            Intent broadcastIntent = new Intent("com.shivomthakkar.miniproject.NetworkChangeReceiver");
            sendBroadcast(broadcastIntent);

            connectToWifi();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();


        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_IMEI_PERMISSION);
            Log.i(TAG, "Requesting permissions...");
        }
    }

    public void connectToWifi(){
        try{
            WifiConfiguration wifiConfiguration = new WifiConfiguration();
            wifiConfiguration.SSID = prefs.getString("ConnectTo", "");
            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            int netId = wifiManager.addNetwork(wifiConfiguration);

            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_IMEI_PERMISSION:
                Log.i(TAG, "Permission granted.");
                getIMEI();

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
