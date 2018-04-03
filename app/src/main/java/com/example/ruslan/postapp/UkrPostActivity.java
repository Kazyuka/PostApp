package com.example.ruslan.postapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.util.Base64;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.provider.SyncStateContract.Constants;
import android.widget.ListView;

public class UkrPostActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String IMAGE_ORDER = "imageOrder";
    static final String UKR_POST = "ukrPost";
    String udiId;
    ArrayList<ImageObject> imagesFromFirebase = new ArrayList<ImageObject>();
    private DatabaseReference mDatabase;
    ImageAdapter dataAdapter;
    SwipeMenuListView ukrPostList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ukr_post);
        Intent intent = getIntent();
        udiId = intent.getStringExtra("uid");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ukrPostList = (SwipeMenuListView) findViewById(R.id.ukrPostList);
        dataAdapter = new ImageAdapter(this, imagesFromFirebase);
        dataAdapter.notifyDataSetChanged();
        ukrPostList.setAdapter(dataAdapter);
        getDataFromServer();
        createSwipe();
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

        ukrPostList.setMenuCreator(creator);

        ukrPostList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        ImageObject data = imagesFromFirebase.get(position);
                        String key =  data.uid;
                        mDatabase.child(IMAGE_ORDER).child(UKR_POST).child(udiId).child(key).removeValue();
                        getDataFromServer();
                        break;
                }
                return false;
            }
        });
    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("image", imageEncoded);

        String key = mDatabase.child(IMAGE_ORDER).child(UKR_POST).child(udiId).push().getKey();
        DatabaseReference date = mDatabase.child(IMAGE_ORDER).child(UKR_POST).child(udiId).child(key);
        String time = NewPostActivity.getCurrentTime();
        childUpdates.put("currentTime", time);
        childUpdates.put("uid", key);
        date.setValue(childUpdates);
        getDataFromServer();
    }

    private void getDataFromServer () {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(IMAGE_ORDER).child(UKR_POST).child(udiId);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> data = (Map<String, Object>) dataSnapshot.getValue();

                if (data != null) {
                    imagesFromFirebase = ImageObject.getArrayData(data);
                    dataAdapter.updateReceiptsList(imagesFromFirebase);
                } else {
                    imagesFromFirebase.clear();
                    dataAdapter.updateReceiptsList(imagesFromFirebase);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            encodeBitmapAndSaveToFirebase(imageBitmap);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photoButtton:
                dispatchTakePictureIntent();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }
}
