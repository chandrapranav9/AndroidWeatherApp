package com.example.pranav.hw05_1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int REQ_CODE = 1;
    ArrayList<City> cities_list  = new ArrayList<City>();
    TextView textView_nocity;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout_cities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Homework 5");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        linearLayout_cities = new LinearLayout(MainActivity.this);
        linearLayout_cities.setOrientation(LinearLayout.VERTICAL);
        linearLayout_cities.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_new){
            //Toast.makeText(this,"Pressed Add",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, AddCity.class);
            startActivityForResult(intent, REQ_CODE);
            relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
            linearLayout_cities.removeAllViews();
            textView_nocity = (TextView) findViewById(R.id.TextView_nocity);
            textView_nocity.setVisibility(View.GONE);
            relativeLayout.removeView(linearLayout_cities);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            City city = (City) data.getExtras().getSerializable("CITY_KEY");
            Log.d("info","City Name: "+city.getCityName()+" State Initial: "+city.getStateInitials());
            cities_list.add(city);
            Log.d("info","Added city. Size of arraylist is: "+cities_list.size());
            setLayout();
        }
    }

    private void setLayout() {
        //textView_nocity = (TextView) findViewById(R.id.TextView_nocity);
        //textView_nocity.setVisibility(View.GONE);
        linearLayout_cities.setVisibility(View.VISIBLE);

        for(int i=0;i<cities_list.size();i++){
            final TextView textView_cityadded = new TextView(MainActivity.this);
            textView_cityadded.setTextSize(20);
            final City city = cities_list.get(i);
            textView_cityadded.setText(city.getCityName().replaceAll("_", " "));
            Log.d("info", city.getCityName());
            //textView_cityadded.setBackground(getDrawable(R.drawable.back));
            textView_cityadded.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView_cityadded.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //textView_cityadded.setBackgroundColor(Color.parseColor("#6699ff"));
                    //call the hourly data activity by passing City Name and Initials
                    String city_name = city.getCityName().replaceAll(" ","_");
                    String state_initials = city.getStateInitials();
                    Intent intent = new Intent(MainActivity.this,HourlyDataActivity.class);
                    intent.putExtra("CITYNAME_KEY",city_name);
                    intent.putExtra("STATE_KEY",state_initials);
                    startActivity(intent);
                }
            });

            textView_cityadded.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //textView_cityadded.setBackgroundColor(Color.parseColor("#6699ff"));
                    cities_list.remove(city);
                    Log.d("info", "Removed element. Size of new arraylist is: " + cities_list.size());
                    textView_cityadded.setVisibility(View.GONE);
                    if (cities_list.size() == 0) {
                        textView_nocity = (TextView) findViewById(R.id.TextView_nocity);
                        textView_nocity.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
            });

            linearLayout_cities.addView(textView_cityadded);
        }
        relativeLayout.addView(linearLayout_cities);
    }
}
