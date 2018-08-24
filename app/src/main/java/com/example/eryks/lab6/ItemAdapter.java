package com.example.eryks.lab6;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ItemAdapter extends ArrayAdapter<File>{


    private List items;
    public ItemAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ItemAdapter(Context context, int resource, List<File> items) {
        super(context, resource, items);
        this.items=items;
    }

    @Nullable
    @Override
    public File getItem(int position) {
        return super.getItem(position);
    }
    public String getName(int position){
        return getItem(position).getName();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row_item, null);
        }

        File p = (File) items.get(position);


        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.name);


            if (tt1 != null) {
                tt1.setText(p.getName().substring(0, p.getName().length() - 4));
            }

            TextView tt2 = (TextView) v.findViewById(R.id.status_info);


            if (tt2 != null) {
                SimpleDateFormat modif = new SimpleDateFormat("yyyy.MM.dd  'o' HH:mm:ss ");
                long d = p.lastModified()+7200000;
                Date lastModDate = new Date(d);
                String desc = "Modyfikowano: "+ modif.format(lastModDate);
                tt2.setText(desc);
            }

        }

        return v;
    }

}