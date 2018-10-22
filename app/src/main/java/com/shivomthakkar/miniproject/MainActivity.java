package com.shivomthakkar.miniproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SalutDataCallback, View.OnClickListener{

    public SalutDataReceiver dataReceiver;
    public SalutServiceData serviceData;
    public Salut network;
    Button btnStudent;
    String TAG = "MainActivity";

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIds();

        btnStudent.setOnClickListener(this);

        dataReceiver = new SalutDataReceiver(this, this);
        serviceData = new SalutServiceData("HOST", 45656,
                "HOST");
        network = new Salut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                // wiFiFailureDiag.show();
                // OR
                Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
            }
        });

    }

    void getIds() {
        btnStudent = (Button) findViewById(R.id.btnStudent);
        prefs = getSharedPreferences("AttendanceLogger", Context.MODE_PRIVATE);
    }

    private void discoverServices()
    {
        Log.d(TAG, "Running as host: "+network.isRunningAsHost);
        Log.d(TAG, "Running discovery: "+network.isDiscovering);
        if(!network.isRunningAsHost && !network.isDiscovering)
        {
            network.discoverNetworkServices(new SalutCallback() {
                @Override
                public void call() {
                    Log.d(TAG, "Call method for Discovery.");
                    for(int i=0;i<network.foundDevices.size();i++)
                    {
                        Log.d(TAG,network.foundDevices.get(i).toString());
                    }
                    Toast.makeText(MainActivity.this, "Device: " + network.foundDevices.get(0).instanceName + " found.", Toast.LENGTH_SHORT).show();
                    network.registerWithHost(network.foundDevices.get(0), new SalutCallback() {
                        @Override
                        public void call() {
                            sendData();
                        }
                    }, new SalutCallback() {
                        @Override
                        public void call() {
                            Log.d(TAG, "We failed to register.");
                        }
                    });
                }
            }, true);
        }
        else
        {
            network.stopServiceDiscovery(true);
        }
    }


    public void sendData()
    {
        AttendanceData obj = new AttendanceData();

        obj.nameOfStudent = prefs.getString("Name", "");
        obj.rollNo = prefs.getInt("Roll No", 9999);
        obj.studentYear = prefs.getString("Year", "");
        obj.studentDivision = prefs.getString("Division", "");
        obj.IMEI = prefs.getString("IMEI", "");
        network.sendToHost(obj, new SalutCallback() {
            @Override
            public void call() {
                Log.d(TAG,"Failed to send.");
            }
        });
    }

    @Override
    public void onDataReceived(Object o) {
        String jsonString = String.valueOf(o);
        Log.d(TAG,jsonString);
        try {
            AttendanceData attendanceData = LoganSquare.parse((String)o , AttendanceData.class);
            Toast.makeText(this, attendanceData.nameOfStudent + " is present.", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(!Salut.isWiFiEnabled(this))
        {
            Toast.makeText(this, "Please enable WiFi first.", Toast.LENGTH_SHORT).show();
        }
        if(v.getId() == R.id.btnStudent)
        {
            discoverServices();
        }
    }

}
