package com.example.ruslan.postapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import java.util.ArrayList;

public class DataCreate {

        public String date;
        public String uid;
        public Date currentTime;

        public DataCreate (String data, String uid, String time) {
                this.date = data;
                this.uid = uid;
                getData(time);
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
        public DataCreate() {

        }

        static public ArrayList<DataCreate> getArrayData (Map<String,Object> obj) {

               ArrayList<DataCreate> dateAarray = new ArrayList<DataCreate>();

               for (Object dataCreate : obj.values()) {
                       Map<String, Object> ob = (Map<String, Object>) dataCreate;
                       String dateString = ob.get("data").toString();
                       String id = ob.get("uid").toString();
                       String time = ob.get("currentTime").toString();
                       DataCreate d = new DataCreate(dateString,id,time);
                       dateAarray.add(d);
               }
                return  dateAarray;
        }
}
