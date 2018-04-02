package com.example.ruslan.postapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Canvas;
import android.widget.ListView;
import android.media.ExifInterface;

import java.io.IOException;
import java.io.FileNotFoundException;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.database.Cursor;

public class NewPostActivity extends AppCompatActivity  {

    ArrayList<ImageObject> imagesFromFirebase = new ArrayList<ImageObject>();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String IMAGE_ORDER = "imageOrder";
    static final String NEW_POST = "newPost";
    String udiId;
    private StorageReference mStorege;
    private DatabaseReference mDatabase;
    ListView mewPostListView;
    ImageAdapter dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Intent intent = getIntent();
        udiId = intent.getStringExtra("uid");
        mStorege = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        mewPostListView = (ListView) findViewById(R.id.newPostList);
        dataAdapter = new ImageAdapter(this, imagesFromFirebase);
        dataAdapter.notifyDataSetChanged();
        mewPostListView.setAdapter(dataAdapter);
        getDataFromServer();
    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("image", imageEncoded);

        String key = mDatabase.child(IMAGE_ORDER).push().getKey();
        DatabaseReference date = mDatabase.child(IMAGE_ORDER).child(NEW_POST).child(udiId);
        childUpdates.put("uid", key);
        date.push().setValue(childUpdates);
        getDataFromServer();
    }
    private void getDataFromServer () {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(IMAGE_ORDER).child(NEW_POST).child(udiId);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> data = (Map<String, Object>) dataSnapshot.getValue();

                if (data != null) {
                    imagesFromFirebase = ImageObject.getArrayData(data);
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



