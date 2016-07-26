package com.example.asmaa.weather_application.Models;

import java.io.Serializable;

public class WeatherForecast implements Serializable{
    private final String mLocationName;
    private final long mTimestamp;
    private final String mDescription;
    private final float mMinimumTemperature;
    private final float mMaximumTemperature;

    private  float mNight;
    private  float mEven;
    private  float mMorn;



    private double pressure;
    private double humidity;
    private float speed;
    private int clouds;
    private int rain;


    public WeatherForecast(final String locationName,
                           final long timestamp,
                           final String description,
                           final float minimumTemperature,
                           final float maximumTemperature) {

        mLocationName = locationName;
        mTimestamp = timestamp;
        mMinimumTemperature = minimumTemperature;
        mMaximumTemperature = maximumTemperature;
        mDescription = description;

    }


    public WeatherForecast(final String locationName,
                           final long timestamp,
                           final String description,
                           final float minimumTemperature,
                           final float night,final float even,final float morning
    ,final float maximumTemperature,float speed,int clouds,int rain,double pressure,double humidity) {

        mLocationName = locationName;
        mTimestamp = timestamp;
        mMinimumTemperature = minimumTemperature;
        mMaximumTemperature = maximumTemperature;
        mDescription = description;
        mNight=night;
        mEven=even;
        mMorn=morning;
        this.speed=speed;
        this.clouds=clouds;
        this.rain=rain;
        this.pressure=pressure;
        this.humidity=humidity;
    }

    public String getLocationName() {
        return mLocationName;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public String getDescription() {
        return mDescription;
    }

    public float getMinimumTemperature() {
        return mMinimumTemperature;
    }

    public float getMaximumTemperature() {
        return mMaximumTemperature;
    }

    public String getmLocationName() {
        return mLocationName;
    }

    public long getmTimestamp() {
        return mTimestamp;
    }

    public String getmDescription() {
        return mDescription;
    }

    public float getmMinimumTemperature() {
        return mMinimumTemperature;
    }

    public float getmMaximumTemperature() {
        return mMaximumTemperature;
    }

    public float getmNight() {
        return mNight;
    }

    public float getmEven() {
        return mEven;
    }

    public float getmMorn() {
        return mMorn;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public float getSpeed() {
        return speed;
    }

    public float getClouds() {
        return clouds;
    }

    public float getRain() {
        return rain;
    }
}