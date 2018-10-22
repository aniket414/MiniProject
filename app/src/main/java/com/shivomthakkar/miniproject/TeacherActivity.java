package com.shivomthakkar.miniproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.List;

public class TeacherActivity extends AppCompatActivity implements SalutDataCallback {

    public SalutDataReceiver dataReceiver;
    public SalutServiceData serviceData;
    public Salut network;

    String year = "", division = "", time = "";

    DatabaseHandler db;

    ListView lVStudents;
    TextView tVLabel;

    String TAG = "TeacherActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        getIdsAndInstantiate();

        Intent i = getIntent();
        year = i.getStringExtra("Year");
        time = i.getStringExtra("Time");
        division = i.getStringExtra("Division");


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

    void getIdsAndInstantiate() {
        tVLabel = (TextView) findViewById(R.id.tVTeacherLabel);
        lVStudents = (ListView) findViewById(R.id.lVStudents);

        db = new DatabaseHandler(this);

    }

    private void setupNetwork()
    {
        if(!network.isRunningAsHost)
        {
            network.startNetworkService(new SalutDeviceCallback() {
                @Override
                public void call(SalutDevice salutDevice) {
                    Toast.makeText(TeacherActivity.this, "Device: " + salutDevice.instanceName + " connected.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            network.stopNetworkService(false);
        }
    }


    @Override
    public void onDataReceived(Object o) {
        try {
            AttendanceData attendanceData = LoganSquare.parse((String)o , AttendanceData.class);
            db.addStudent(new Student(attendanceData.rollNo, attendanceData.nameOfStudent, attendanceData.IMEI, time, year+" - "+division));
            List<Student> list = db.getAllStudents();
            StudentListAdapter adapter = new StudentListAdapter(this, list);
            tVLabel.setVisibility(View.GONE);
            lVStudents.setVisibility(View.VISIBLE);
            lVStudents.setAdapter(adapter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.teacher_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_start:
                if(!Salut.isWiFiEnabled(this)) {
                    Toast.makeText(this, "Please enable WiFi first.", Toast.LENGTH_SHORT).show();
                } else {
                    setupNetwork();
                }
                break;
            case R.id.menu_delete:
                db.deleteAll();
                List<Student> list = db.getAllStudents();
                StudentListAdapter adapter = new StudentListAdapter(this, list);
                lVStudents.setAdapter(adapter);
                if(list.size() != 0) {
                    tVLabel.setVisibility(View.GONE);
                    lVStudents.setVisibility(View.VISIBLE);
                } else {
                    tVLabel.setVisibility(View.VISIBLE);
                    lVStudents.setVisibility(View.GONE);
                }
                break;
            default: break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        network.stopNetworkService(false);

    }
}
