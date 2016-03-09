package com.example.pranav.hw05_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Pranav on 05-03-2016.
 */
public class WeatherAdapter extends ArrayAdapter<Weather> {

    List<Weather> mData;
    Context mContext;
    int mResource;

    public WeatherAdapter(Context context, int resource, List<Weather> objects) {
        super(context, resource, objects);
        this.mContext=context;
        this.mData=objects;
        this.mResource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }

        Weather weather=mData.get(position);
        String time_string = new String();

        ImageView imageView_icon = (ImageView) convertView.findViewById(R.id.imageView_icon);
        TextView textView_time = (TextView) convertView.findViewById(R.id.textView_time);
        int time=0;
        if(Integer.parseInt(weather.getTime()) >= 12){
            time_string="PM";
            time=Integer.parseInt(weather.getTime())%12;
            if(Integer.parseInt(weather.getTime())==12){
                time=12;
            }
        }
        else{
            time_string="AM";
            time=Integer.parseInt(weather.getTime());
        }
        textView_time.setText(time + ":00"+time_string);
        TextView textView_condition = (TextView) convertView.findViewById(R.id.textView_condition);
        textView_condition.setText(weather.getClouds());
        TextView textView_temp = (TextView) convertView.findViewById(R.id.textView_temp);
        textView_temp.setText(weather.getTemperature()+"°F");
        Picasso.with(mContext).load(weather.getIconURL()).into(imageView_icon);


        return convertView;
    }
}
