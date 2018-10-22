package com.shivomthakkar.miniproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Shivom on 10/19/17.
 */

public class StudentListAdapter extends ArrayAdapter<Student> {

    private final Context context;
    private final List<Student> values;

    public StudentListAdapter(Context context, List<Student> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    static class ViewHolder{
        public TextView stName, rollNo, sTClass, IMEI;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.listview_item, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.stName = (TextView) rowView.findViewById(R.id.tVLVName);
            viewHolder.rollNo = (TextView) rowView.findViewById(R.id.tVLVRollNo);
            viewHolder.IMEI = (TextView) rowView.findViewById(R.id.tVLVImei);
            viewHolder.sTClass = (TextView) rowView.findViewById(R.id.tVLVClass);
            rowView.setTag(viewHolder);

        }

        ViewHolder viewHolder = (ViewHolder) rowView.getTag();

        viewHolder.stName.setText("Name: " + values.get(position).getName());
        viewHolder.rollNo.setText("Roll No. " + values.get(position).getRollNo());
        viewHolder.IMEI.setText("IMEI #: " + values.get(position).getImei());
        viewHolder.sTClass.setText(values.get(position).getStClass());

        return rowView;

    }

}
