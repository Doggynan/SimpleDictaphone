package com.example.sapij.lab5b;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sapij on 08.05.2018.
 */

public class ItemAdapter extends ArrayAdapter<Item>{

    private ArrayList<Item> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtStatus;
    }

    public ItemAdapter(ArrayList<Item> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtStatus = (TextView) convertView.findViewById(R.id.status_info);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.txtName.setText(item.getName());
        viewHolder.txtStatus.setText(item.getStatus());
        // Return the completed view to render on screen
        return convertView;
    }
}