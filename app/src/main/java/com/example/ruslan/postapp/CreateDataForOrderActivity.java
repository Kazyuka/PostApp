package com.example.ruslan.postapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateDataForOrderActivity extends Activity implements View.OnClickListener  {

    CalendarView calendarView;
    TextView currentDate;
    Button saveButton;
    String dateCreate;
    String currentTime;
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
        saveButton.setVisibility(View.INVISIBLE);
        getDataFromCalendar();
    }

    public void getDataFromCalendar() {

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {
                saveButton.setVisibility(View.VISIBLE);
                currentTime = getCurrentTime();
                dateCreate = "Список отправок:" +" " + (getNameMonth(month+1)) + " / " + date + " / " + year;
            }
        });
    }

    public static String getCurrentTime() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    private String getNameMonth(int number) {
        switch(number) {
            case 1:
                return "Январь";
            case 2:
                return "Феврфль";
            case 3:
                return "Март";
            case 4:
                return "Апрель";
            case 5:
                return "Март";
            case 6:
                return "Июнь";
            case 7:
                return "Июль";
            case 8:
                return "Август";
            case 9:
                return "Сентябрь";
            case 10:
                return "Октябрь";
            case 11:
                return "Ноябрь";
            case 12:
                return "Декабрь";
            default:
                return "";
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                saveButton.setVisibility(View.INVISIBLE);
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("data", dateCreate);
                childUpdates.put("currentTime", currentTime);
                String key = mDatabase.child("createDataOrder").push().getKey();
                DatabaseReference date = mDatabase.child("createDataOrder").child(key);
                childUpdates.put("uid", key);
                date.updateChildren(childUpdates);
                showToast(v);
                break;
            default:break;
        }
    }

    public void showToast(View view) {
        //создаем и отображаем текстовое уведомление
        Toast toast = Toast.makeText(getApplicationContext(),
                "Вы создали новый заказ",
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
