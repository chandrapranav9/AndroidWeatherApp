package com.example.pranav.hw05_1;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WeatherDetails extends AppCompatActivity {

    ArrayList<Weather> weatherArrayList = new ArrayList<>();
    int position=0;
    String CityName;
    String StateInitials;
    TextView textView_location;
    TextView textView_temp;
    TextView textView_cond;
    TextView textView_feelslikevalue;
    TextView textView_humidityvalue;
    TextView textView_dewpointvalue;
    TextView textView_pressurevalue;
    TextView textView_cloudsvalue;
    TextView textView_windsvalue;
    ImageView imageView_icon;
    ImageButton imageButton_next;
    ImageButton imageButton_prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        setTitle("Details Activity");

        textView_location = (TextView) findViewById(R.id.textView_location);
        textView_temp = (TextView) findViewById(R.id.textView_temperature);
        textView_cond = (TextView) findViewById(R.id.textView_condition);
        TextView textView_maxtemp = (TextView) findViewById(R.id.textView_maxTemp);
        TextView textView_mintemp = (TextView) findViewById(R.id.textView_minTemp);
        textView_feelslikevalue = (TextView) findViewById(R.id.textView_feelslikeTemp);
        textView_humidityvalue = (TextView) findViewById(R.id.textView_humidityValue);
        textView_dewpointvalue = (TextView) findViewById(R.id.textView_dewpointValue);
        textView_pressurevalue = (TextView) findViewById(R.id.textView_pressureValue);
        textView_cloudsvalue = (TextView) findViewById(R.id.textView_cloudsValue);
        textView_windsvalue = (TextView) findViewById(R.id.textView_windsValue);
        imageView_icon = (ImageView) findViewById(R.id.imageView_weatherIcon);

        Bundle bundle = getIntent().getExtras();
        weatherArrayList = (ArrayList<Weather>) bundle.getSerializable("LIST_KEY");
        position = getIntent().getExtras().getInt("POSITION_KEY");
        CityName = getIntent().getExtras().getString("CITY_KEY");
        StateInitials = getIntent().getExtras().getString("STATE_KEY");

        Weather w = weatherArrayList.get(0);

        textView_maxtemp.setText(w.getMaxTemp() + " Farenheit");
        textView_mintemp.setText(w.getMinTemp() + " Farenheit");

        SetLayout(position);

        imageButton_next = (ImageButton) findViewById(R.id.imageButton_next);
        imageButton_prev = (ImageButton) findViewById(R.id.imageButton_previous);

        findViewById(R.id.imageButton_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if (position >= weatherArrayList.size()) {
                    position = 0;
                }
                SetLayout(position);
            }
        });

        findViewById(R.id.imageButton_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                if (position <= 0) {
                    position = weatherArrayList.size() - 1;
                }
                SetLayout(position);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_LEFT:
                position--;
                if (position <= 0) {
                    position = weatherArrayList.size() - 1;
                }
                SetLayout(position);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                position++;
                if (position >= weatherArrayList.size()) {
                    position = 0;
                }
                SetLayout(position);
                break;
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
            case KeyEvent.KEYCODE_DEL:
                finish();
                break;
            default:break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void SetLayout(int position) {
        Weather weather = weatherArrayList.get(position);
        String time_string = new String();
        int time=0;
        if(Integer.parseInt(weather.getTime()) >= 12) {
            time_string = "PM";
            time = Integer.parseInt(weather.getTime()) % 12;
            if(Integer.parseInt(weather.getTime())==12){
                time=12;
            }
        }
        else{
            time_string="AM";
            time=Integer.parseInt(weather.getTime());
        }
        Picasso.with(this).load(weather.getIconURL()).into(imageView_icon);
        textView_location.setText(CityName + ", " + StateInitials + " (" + time + ":00"+time_string+")");
        textView_temp.setText(weather.getTemperature() + "°F");
        textView_cond.setText(weather.getClimateType());
        textView_feelslikevalue.setText(weather.getFeelsLike() + " Farenheit");
        textView_humidityvalue.setText(weather.getHumidity() + "%");
        textView_dewpointvalue.setText(weather.getDewpoint() + " Farenheit");
        textView_pressurevalue.setText(weather.getPressure() + " hPa");
        textView_cloudsvalue.setText(weather.getClouds());
        textView_windsvalue.setText(weather.getWindSpeed()+"mph "+weather.getWindDirection());
    }

}
