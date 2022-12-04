package com.gmastik.tanfi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CalendarView;

public class Calendar extends AppCompatActivity {

    // variables
    CalendarView cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // find id
        cv = findViewById(R.id.cv);

        cv.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String date = dayOfMonth+"."+month+"."+year;
            String getDay = String.valueOf(dayOfMonth);
            String getMonth = String.valueOf(month);
            String getYear = String.valueOf(year);
            String condition = "true";
            Intent intent = new Intent(getApplicationContext(),AddTask.class);
            intent.putExtra("date",date);
            intent.putExtra("day",getDay);
            intent.putExtra("month",getMonth);
            intent.putExtra("year",getYear);
            intent.putExtra("condition",condition);
            startActivity(intent);
        });
    }
}