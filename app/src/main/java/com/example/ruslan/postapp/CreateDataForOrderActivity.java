package com.example.ruslan.postapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateDataForOrderActivity extends Activity implements View.OnClickListener  {

    CalendarView calendarView;
    TextView currentDate;
    Button saveButton;
    String dateCreate;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_data_for_order);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        currentDate = (TextView) findViewById(R.id.currentDate);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        getDataFromCalendar();
    }

    public void getDataFromCalendar() {

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {

                dateCreate = "Список отправок созданный:  " + " " + date +" / " + (month+1) + " / " + year;
            }
        });
    }

    @Override
    public void onClick(View v) {
        // по id определеяем кнопку, вызвавшую этот обработчик
        switch (v.getId()) {
            case R.id.saveButton:
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("data", dateCreate);
                String key = mDatabase.child("createDataOrder").push().getKey();
                DatabaseReference date = mDatabase.child("createDataOrder").child(key);
                childUpdates.put("uid", key);
                date.updateChildren(childUpdates);
                break;

            default:break;

        }
    }

}
