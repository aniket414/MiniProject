package com.shivomthakkar.miniproject;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class TeacherSettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spYear, spDivision, spTime;
    Button btnOkay;
    TextInputEditText eTTeacherPassword;
    AutoCompleteTextView eTTeacherId;

    boolean isYearSelected = false, isDivisionSelected = false, isTimeSelected = false;
    String year = "", division = "", time = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_settings);

        getIds();


        List<String> yearList = new ArrayList<>();
        yearList.add("Select a year");
        yearList.add("First Year");
        yearList.add("Second Year");
        yearList.add("Third Year");
        yearList.add("Fourth Year");

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(yearAdapter);


        List<String> divisionList = new ArrayList<>();
        divisionList.add("Select a division");
        divisionList.add("A");
        divisionList.add("B");
        divisionList.add("C");
        divisionList.add("D");
        divisionList.add("E");
        divisionList.add("F");

        ArrayAdapter<String> divisionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, divisionList);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDivision.setAdapter(divisionAdapter);


        List<String> timeList = new ArrayList<>();
        timeList.add("Select a time period");
        timeList.add("10:00");
        timeList.add("11:00");
        timeList.add("12:00");
        timeList.add("13:35");
        timeList.add("14:35");
        timeList.add("15:45");
        timeList.add("16:40");

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timeList);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTime.setAdapter(timeAdapter);


        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(isYearSelected && isTimeSelected && isDivisionSelected) {
                    //TODO - Setup an Oracle server and validate the teacher's credentials.
                //}
                if(!time.equals("") && !year.equals("") && !division.equals("")) {
                    Intent intent = new Intent(TeacherSettingsActivity.this, TeacherActivity.class);
                    intent.putExtra("Time", time);
                    intent.putExtra("Division", division);
                    intent.putExtra("Year", year);
                    startActivity(intent);
                }

            }
        });

    }

    void getIds() {
        spYear = (Spinner) findViewById(R.id.spYear);
        spYear.setOnItemSelectedListener(this);
        spTime = (Spinner) findViewById(R.id.spTime);
        spTime.setOnItemSelectedListener(this);
        spDivision = (Spinner) findViewById(R.id.spDivision);
        spDivision.setOnItemSelectedListener(this);
        eTTeacherId = (AutoCompleteTextView) findViewById(R.id.eTTeacherId);
        eTTeacherPassword = (TextInputEditText) findViewById(R.id.eTTeacherPassword);
        btnOkay = (Button) findViewById(R.id.btnOkay);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO - Check this.

        Spinner spinner = (Spinner) adapterView;
        if(spinner.getId() == R.id.spDivision) {
            division = adapterView.getItemAtPosition(i).toString();
            isDivisionSelected = true;
        } else if(spinner.getId() == R.id.spTime) {
            time = adapterView.getItemAtPosition(i).toString();
            isTimeSelected = true;

        } else if(spinner.getId() == R.id.spYear) {
            year = adapterView.getItemAtPosition(i).toString();
            isYearSelected = true;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
