package com.example.ruslan.postapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.tasks.OnFailureListener;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

public class ListOrdersByDataActivity extends AppCompatActivity {

    ArrayList<DataCreate> dataCreateOrder = new ArrayList<DataCreate>();
    ArrayList<DataCreate> dataCreateSorted = new ArrayList<DataCreate>();
    SwipeMenuListView tableView;
    DataAdapter dataAdapter;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders_by_data);
        // находим список
        tableView = (SwipeMenuListView) findViewById(R.id.tableView);
        dataAdapter = new DataAdapter(this, dataCreateOrder);
        dataAdapter.notifyDataSetChanged();
        tableView.setAdapter(dataAdapter);
        checkInternetCinnection();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        tableView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                DataCreate data = dataCreateOrder.get(position);
                String s =  data.uid;
                Intent intent = new Intent(view.getContext(),DalyOrders.class);
                intent.putExtra("uid", data.uid);
                startActivity(intent);
            }
        });

        createSwipe();
        getDataFromServer();
    }


    private  void checkInternetCinnection() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {

                } else {
                    showToast( "Нет интернет соединения");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }

    private void createSwipe() {

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                openItem.setWidth(150);
                // set item title
                openItem.setTitle("Delete");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

            }
        };

        tableView.setMenuCreator(creator);

        tableView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        DataCreate data = dataCreateOrder.get(position);
                        String key =  data.uid;
                        mDatabase.child(key).removeValue();
                        getDataFromServer();
                        break;
                }
                return false;
            }
        });
    }


    private void getDataFromServer () {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("createDataOrder");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> data = (Map<String, Object>) dataSnapshot.getValue();

                if (data != null) {
                    dataCreateOrder = DataCreate.getArrayData(data);
                    dataAdapter.updateReceiptsList(dataCreateOrder);
                } else {
                    dataCreateOrder.clear();
                    dataAdapter.updateReceiptsList(dataCreateOrder);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });
    }

    public void showToast(String text) {
        //создаем и отображаем текстовое уведомление
        Toast toast = Toast.makeText(getApplicationContext(),
                text,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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
