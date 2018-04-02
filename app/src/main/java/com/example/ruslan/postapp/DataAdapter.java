package com.example.ruslan.postapp;

/**
 * Created by ruslan on 30.03.2018.
 */
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class DataAdapter  extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<DataCreate> objects;

    DataAdapter(Context context, ArrayList<DataCreate> products) {
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
            view = lInflater.inflate(R.layout.item_data, parent, false);
        }

        DataCreate p = getProduct(position);
        ((TextView) view.findViewById(R.id.tvDescr)).setText(p.date);

        return view;
    }


    DataCreate getProduct(int position) {
        return ((DataCreate) getItem(position));
    }

    public void updateReceiptsList(List<DataCreate> newlist) {
        objects.clear();
        objects.addAll(newlist);
        this.notifyDataSetChanged();
    }
}