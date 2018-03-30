package com.example.ruslan.postapp;

import java.util.Map;

import java.util.ArrayList;

public class DataCreate {

        public String date;
        public String uid;

        public DataCreate (String data, String uid) {
                this.date = data;
                this.uid = uid;
        }

        public DataCreate() {

        }

        static public ArrayList<DataCreate> getArrayData (Map<String,Object> obj) {

               ArrayList<DataCreate> dateAarray = new ArrayList<DataCreate>();

               for (Object dataCreate : obj.values()) {
                       Map<String, Object> ob = (Map<String, Object>) dataCreate;
                       String dateString = ob.get("data").toString();
                       String id = ob.get("uid").toString();
                       DataCreate d = new DataCreate(dateString,id);
                       dateAarray.add(d);
               }
                return  dateAarray;
        }
}
