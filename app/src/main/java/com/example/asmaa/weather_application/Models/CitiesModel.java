package com.example.asmaa.weather_application.Models;

import com.example.asmaa.weather_application.Services.CitiesService;

import java.util.List;

/**
 * Created by asmaa on 31/07/16.
 */

public class CitiesModel {
    private  String mCityName;
    private  double longg;
    private  double latt;


    public CitiesModel(final String CityName,
                       final double Coordlong,final double CoordLatt) {

        mCityName = CityName;
        longg = Coordlong;
        latt=CoordLatt;

    }

    public String getmCityName() {
        return mCityName;
    }

    public double getLongg() {
        return longg;
    }

    public double getLatt() {
        return latt;
    }
}
