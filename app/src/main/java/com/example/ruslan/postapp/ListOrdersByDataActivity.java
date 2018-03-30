package com.example.ruslan.postapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.StreamDownloadTask;

import android.util.Log;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ListOrdersByDataActivity extends AppCompatActivity {

    ArrayList<DataCreate> dataCreateOrder = new ArrayList<DataCreate>();
    ListView tableView;
    DataAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders_by_data);
        // находим список
        tableView = (ListView) findViewById(R.id.tableView);
        dataAdapter = new DataAdapter(this, dataCreateOrder);
        dataAdapter.notifyDataSetChanged();
        tableView.setAdapter(dataAdapter);

        tableView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                //String day = dataCreateOrder.get(position);
                Intent intent = new Intent(view.getContext(),DalyOrders.class);
                //intent.putExtra("uid", day);
                startActivity(intent);
            }
        });

        getDataFromServer();
    }


    private void getDataFromServer () {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("createDataOrder");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> data = (Map<String, Object>) dataSnapshot.getValue();
                dataCreateOrder = DataCreate.getArrayData(data);
                dataAdapter.updateReceiptsList(dataCreateOrder);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(this, CreateDataForOrderActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
