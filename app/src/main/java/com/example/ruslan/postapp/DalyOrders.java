package com.example.ruslan.postapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class DalyOrders extends Activity {

    String udiId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daly_orders);
        Intent intent = getIntent();
        udiId = intent.getStringExtra("uid");
    }
}
