package com.shivomthakkar.miniproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spFilter, spOptions;
    ListView lVFilter;
    String CHOICE = "";

    DatabaseHandler db;

    List<String> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        getIds();

        List<String> filterList = new ArrayList<>();
        filterList.add("By time");
        filterList.add("By class");

        options = new ArrayList<>();

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filterList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilter.setAdapter(yearAdapter);


    }

    void getIds() {
        spFilter = (Spinner) findViewById(R.id.spFilter);
        spFilter.setOnItemSelectedListener(this);
        spOptions = (Spinner) findViewById(R.id.spOptions);
        spOptions.setOnItemSelectedListener(this);
        lVFilter = (ListView) findViewById(R.id.lVFilter);

        db = new DatabaseHandler(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        String item;
        if(spinner.getId() == R.id.spOptions) {
            item = adapterView.getItemAtPosition(i).toString();
            List<Student> studentList;
            if(CHOICE.equals("CLASS")) {
                studentList = db.getListByClass(item);
                StudentListAdapter adapter = new StudentListAdapter(this, studentList);
                lVFilter.setAdapter(adapter);
            } else if(CHOICE.equals("TIME")) {
                studentList = db.getListByTime(item);
                StudentListAdapter adapter = new StudentListAdapter(this, studentList);
                lVFilter.setAdapter(adapter);
            }
        } else if(spinner.getId() == R.id.spFilter) {
            item = adapterView.getItemAtPosition(i).toString();
            if(item.equals("By time")) {
                options.clear();
                options.add("Select a time period");
                options.add("10:00");
                options.add("11:00");
                options.add("12:00");
                options.add("13:35");
                options.add("14:35");
                options.add("15:45");
                options.add("16:40");
                CHOICE = "TIME";
            } else if(item.equals("By class")) {
                options.clear();
                options.add("Select a class");
                options.add("Third Year - A");
                options.add("Third Year - B");
                CHOICE = "CLASS";
            }
            ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);
            yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spOptions.setAdapter(yearAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
