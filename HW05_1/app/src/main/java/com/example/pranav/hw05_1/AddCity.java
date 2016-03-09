package com.example.pranav.hw05_1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddCity extends AppCompatActivity {

    String input_cityName = new String();
    String input_stateInitial = new String();
    String KEYID = new String("108b1b8381644a7b");
    int errorFlag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        setTitle("Add City Activity");

        final EditText editText_cityname = (EditText) findViewById(R.id.editText_CityName);
        final EditText editText_stateinitial = (EditText) findViewById(R.id.editText_StateInitial);

        findViewById(R.id.button_SaveCity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_cityName = editText_cityname.getText().toString();
                input_stateInitial = editText_stateinitial.getText().toString();
                String s = input_cityName.replaceAll(" ","_");
                input_cityName=s;
                Log.d("info", "Input city is: " + input_cityName + " Input State Initials are: " + input_stateInitial);
                String url = new String ("http://api.wunderground.com/api/"+KEYID+"/hourly/q/"+input_stateInitial+"/"+input_cityName+".xml");
                Log.d("info","URL is: "+url);
                new ValidateInput().execute(url);
            }
        });
    }

    class ValidateInput extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                int statusCode = con.getResponseCode();
                InputStream is = con.getInputStream();
                String errorString = new String();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                StringBuilder stringBuilder = new StringBuilder();
                String line = new String();
                while((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    if(line.contains("querynotfound") || line.contains("country_iso3166")){errorFlag=1;}
                    //Log.d("info",line);
                }
                errorString = stringBuilder.toString();
                /*
                if(errorString.contains("No cities match your search query")){
                    errorFlag=1;
                }
                else{errorFlag=0;}*/

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(errorFlag == 1) {
                Toast.makeText(AddCity.this, "City or State are input incorrectly.", Toast.LENGTH_LONG).show();
            }
            else{
                //Toast.makeText(AddCity.this, "Valid Input", Toast.LENGTH_LONG).show();
                City city = new City();
                city.setCityName(input_cityName);
                city.setStateInitials(input_stateInitial);
                Intent intent = new Intent();
                intent.putExtra("CITY_KEY",city);
                setResult(RESULT_OK,intent);
                finish();
            }
            errorFlag=0;
        }
    }
}
