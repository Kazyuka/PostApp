package com.example.ruslan.postapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import java.io.IOException;
import android.util.Base64;

/**
 * Created by ruslan on 02.04.2018.
 */

public class ImageObject {

    public String imageFromFirebase;
    public String uid;
    public Bitmap image;
    public Date currentTime;

    public ImageObject(String data, String uid, String currentTime) {
        this.imageFromFirebase = data;
        this.uid = uid;
        this.image = bindRestaurant(data);
        getData(currentTime);
    }

    public void getData(String time)
    {
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date dateObj = curFormater.parse(time);
            this.currentTime = dateObj;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ImageObject() {

    }

    public Bitmap bindRestaurant(String photo) {

        Bitmap imageBitmap = null;
        try {
            imageBitmap = decodeFromFirebaseBase64(photo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageBitmap;
    }

    public Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
    static public ArrayList<ImageObject> getArrayData (Map<String,Object> obj) {

        ArrayList<ImageObject> dateAarray = new ArrayList<ImageObject>();

        for (Object dataCreate : obj.values()) {
            Map<String, Object> ob = (Map<String, Object>) dataCreate;
            String dateString = ob.get("image").toString();
            String id = ob.get("uid").toString();
            String time = ob.get("currentTime").toString();
            ImageObject d = new ImageObject(dateString,id,time);
            dateAarray.add(d);
        }

        Collections.sort(dateAarray, new Comparator<ImageObject>() {
            @Override
            public int compare(ImageObject dataCreate, ImageObject t1) {
                return t1.currentTime.compareTo(dataCreate.currentTime);
            }

        });
        return  dateAarray;
    }
}
