package com.example.ruslan.postapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruslan on 02.04.2018.
 */

public class ImageAdapter  extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ImageObject> objects;

    ImageAdapter(Context context, ArrayList<ImageObject> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_photo, parent, false);
        }

        ImageObject p = getProduct(position);
        ((ImageView) view.findViewById(R.id.imageView)).setImageBitmap(p.image);

        return view;
    }

    ImageObject getProduct(int position) {
        return ((ImageObject) getItem(position));
    }

    public void updateReceiptsList(List<ImageObject> newlist) {
        objects.clear();
        objects.addAll(newlist);
        this.notifyDataSetChanged();
    }
}