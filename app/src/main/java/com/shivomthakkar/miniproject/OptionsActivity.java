package com.shivomthakkar.miniproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLive, btnPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        getIds();
    }

    void getIds() {
        btnLive = (Button) findViewById(R.id.btnLive);
        btnLive.setOnClickListener(this);
        btnPrevious = (Button) findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.btnLive:
                intent = new Intent(OptionsActivity.this, TeacherSettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.btnPrevious:
                intent = new Intent(OptionsActivity.this, FilterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
