package com.example.pranav.hw05_1;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Pranav on 05-03-2016.
 */
public class WeatherUtil {

    static public class PullWeather{

        static ArrayList<Weather> parseWeather(InputStream inputStream) throws XmlPullParserException,IOException{

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(inputStream,"UTF-8");

            int tempFlag=0;
            int dewFlag=0;
            int windFlag=0;
            int windDirFlag=0;
            int pressureFlag=0;
            int feelsLikeFlag=0;
            int mintemp=0;
            int maxtemp=0;

            ArrayList<Weather> weatherList = new ArrayList<>();
            Weather weather = null;
            int event = parser.getEventType();

            while(event != XmlPullParser.END_DOCUMENT){
                switch (event){
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("forecast")){
                            //this is the first weather object.
                            weather = new Weather();
                        }
                        if (parser.getName().equals("hour")){
                            weather.setTime(parser.nextText());
                        }
                        if(parser.getName().equals("temp")){
                            tempFlag=1;
                        }
                        if(parser.getName().equals("english") && tempFlag==1){
                            weather.setTemperature(parser.nextText());
                        }
                        if (parser.getName().equals("dewpoint")){
                            dewFlag=1;
                        }
                        if(parser.getName().equals("english") && dewFlag==1){
                            weather.setDewpoint(parser.nextText());
                        }
                        if(parser.getName().equals("condition")){
                            weather.setClouds(parser.nextText());
                        }
                        if(parser.getName().equals("icon_url")){
                            weather.setIconURL(parser.nextText());
                        }
                        if(parser.getName().equals("wspd")){
                            windFlag=1;
                        }
                        if(parser.getName().equals("english") && windFlag==1){
                            weather.setWindSpeed(parser.nextText());
                        }
                        if(parser.getName().equals("wdir")){
                            windDirFlag=1;
                        }
                        if(parser.getName().equals("dir") && windDirFlag==1){
                            String wdir = parser.nextText();
                            Log.d("info","Wind dir: "+wdir);
                            if(wdir.equals("S")){
                                weather.setWindDirection("South");
                            }
                            else if(wdir.equals("N")){
                                weather.setWindDirection("North");
                            }
                            else if(wdir.equals("E")){
                                weather.setWindDirection("East");
                            }
                            else if(wdir.equals("W")){
                                weather.setWindDirection("West");
                            }
                            else{
                                weather.setWindDirection(wdir);
                            }
                        }
                        if(parser.getName().equals("degrees") && windDirFlag==1){
                            String s = weather.getWindDirection();
                            String s1 = parser.nextText()+"°";
                            String finalstr = new String();
                            finalstr = s1 +" "+s;
                            weather.setWindDirection(finalstr);
                        }
                        if(parser.getName().equals("wx")){
                            weather.setClimateType(parser.nextText());
                        }
                        if(parser.getName().equals("humidity")){
                            weather.setHumidity(parser.nextText());
                        }
                        if(parser.getName().equals("feelslike")){
                            feelsLikeFlag=1;
                        }
                        if(parser.getName().equals("english") && feelsLikeFlag==1){
                            weather.setFeelsLike(parser.nextText());
                        }
                        if(parser.getName().equals("mslp")){
                            pressureFlag=1;
                        }
                        if(parser.getName().equals("metric") && pressureFlag==1){
                            weather.setPressure(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("temp")){
                            tempFlag=0;
                        }
                        if(parser.getName().equals("dewpoint")){
                            dewFlag=0;
                        }
                        if(parser.getName().equals("wspd")){
                            windFlag=0;
                        }
                        if(parser.getName().equals("wdir")){
                            windDirFlag=0;
                        }
                        if(parser.getName().equals("feelslike")){
                            feelsLikeFlag=0;
                        }
                        if (parser.getName().equals("mslp")){
                            pressureFlag=0;
                        }
                        if(parser.getName().equals("forecast")){
                            weatherList.add(weather);
                            Log.d("info",weather.toString());
                        }

                        break;
                    default:break;
                }
                event=parser.next();
            }
            mintemp= Integer.parseInt(weatherList.get(0).getTemperature());
            maxtemp= Integer.parseInt(weatherList.get(0).getTemperature());
            for(Weather w : weatherList){
                if(Integer.parseInt(w.getTemperature()) < mintemp){
                    mintemp=Integer.parseInt(w.getTemperature());
                }
                if(Integer.parseInt(w.getTemperature()) > maxtemp){
                    maxtemp=Integer.parseInt(w.getTemperature());
                }
            }
            for(Weather w : weatherList){
                w.setMaxTemp(String.valueOf(maxtemp));
                w.setMinTemp(String.valueOf(mintemp));
            }

            Log.d("info","Max Temp is: "+maxtemp);
            Log.d("info","Min Temp is: "+mintemp);

            return weatherList;
        }
    }
}
