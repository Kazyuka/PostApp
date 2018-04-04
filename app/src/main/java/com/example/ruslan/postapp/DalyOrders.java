package com.example.ruslan.postapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class DalyOrders extends Activity implements View.OnClickListener {

    String udiId;
    Button newPostButton, ukrPostButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daly_orders);

        Intent intent = getIntent();
        udiId = intent.getStringExtra("uid");

        newPostButton = (Button) findViewById(R.id.newPostButton);
        newPostButton.setOnClickListener(this);

        ukrPostButton = (Button) findViewById(R.id.ukrPostButton);
        ukrPostButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // по id определеяем кнопку, вызвавшую этот обработчик
        switch (v.getId()) {
            case R.id.newPostButton:
                Intent intent = new Intent(this, NewPostActivity.class);
                intent.putExtra("uid", udiId);
                startActivity(intent);
                break;
            case R.id.ukrPostButton:
                Intent intent2 = new Intent(this, UkrPostActivity.class);
                intent2.putExtra("uid", udiId);
                startActivity(intent2);
                break;
            default:break;

        }
    }
}
