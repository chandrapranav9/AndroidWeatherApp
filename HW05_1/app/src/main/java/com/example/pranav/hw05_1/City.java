package com.example.pranav.hw05_1;

import java.io.Serializable;


public class City implements Serializable {
    String cityName;
    String StateInitials;

    public String getStateInitials() {
        return StateInitials;
    }

    public void setStateInitials(String stateInitials) {
        StateInitials = stateInitials;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "City{" +
                "cityName='" + cityName + '\'' +
                ", StateInitials='" + StateInitials + '\'' +
                '}';
    }
}
