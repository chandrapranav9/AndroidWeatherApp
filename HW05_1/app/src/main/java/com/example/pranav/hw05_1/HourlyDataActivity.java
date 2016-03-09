package com.example.pranav.hw05_1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HourlyDataActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    String city_Name;
    String state_Initials;
    String KEYID = new String("108b1b8381644a7b");
    ArrayList<Weather> weatherDisplayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_data);

        setTitle("HourlyData Activity");

        city_Name=getIntent().getExtras().getString("CITYNAME_KEY").toString();
        state_Initials=getIntent().getExtras().getString("STATE_KEY").toString();
        String temp_string = city_Name.replaceAll("_"," ");
        Log.d("info", "CN: " + city_Name + " SI: " + state_Initials);
        String url = new String ("http://api.wunderground.com/api/"+KEYID+"/hourly/q/"+state_Initials+"/"+city_Name+".xml");
        Log.d("info", url);

        TextView textView_location = (TextView) findViewById(R.id.textView_loc);
        textView_location.setText(temp_string+", "+state_Initials);

        new GetWeatherData().execute(url);

    }

    class GetWeatherData extends AsyncTask<String, Void, ArrayList<Weather>> {

        @Override
        protected ArrayList<Weather> doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                int statusCode = con.getResponseCode();
                if(statusCode == HttpURLConnection.HTTP_OK){
                    InputStream is = con.getInputStream();
                    return WeatherUtil.PullWeather.parseWeather(is);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Weather> aVoid) {
            super.onPostExecute(aVoid);
            weatherDisplayList=aVoid;
            for(Weather w : weatherDisplayList){
                Log.d("info",w.toString()+"\n");
            }
            progressDialog.dismiss();

            ListView listView_weatherlist = (ListView) findViewById(R.id.listView_weatherList);
            WeatherAdapter adapter = new WeatherAdapter(HourlyDataActivity.this,R.layout.row_item_layout,weatherDisplayList);
            listView_weatherlist.setAdapter(adapter);
            adapter.setNotifyOnChange(true);

            listView_weatherlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(HourlyDataActivity.this,WeatherDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("LIST_KEY",weatherDisplayList);
                    intent.putExtras(bundle);
                    intent.putExtra("POSITION_KEY",position);
                    String city_string = city_Name.replaceAll("_"," ");
                    String state_string = state_Initials;
                    intent.putExtra("CITY_KEY",city_string);
                    intent.putExtra("STATE_KEY",state_string);
                    startActivity(intent);
                }
            });
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(HourlyDataActivity.this);
            progressDialog.setMessage("Loading Hourly Data.");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            super.onPreExecute();
        }
    }
}
