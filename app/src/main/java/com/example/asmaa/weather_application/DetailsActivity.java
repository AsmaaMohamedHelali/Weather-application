package com.example.asmaa.weather_application;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asmaa.weather_application.Helpers.DayFormatter;
import com.example.asmaa.weather_application.Helpers.TemperatureFormatter;
import com.example.asmaa.weather_application.Models.WeatherForecast;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private WeatherForecast weatherForecast;
    private TextView tvDayName,tvDayDesc,tvDayMin,tvDayMax,tvDayHumidity,tvNight
            ,tvEven,tvMorn,tvrain,tvpressure,tvspeed,tvclouds;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initToolbar();
        callDataFromIntent();
        if(weatherForecast!=null){
            initViews();
            setDataToViews();
        }


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.day_details));
        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void setDataToViews() {
        final DayFormatter dayFormatter = new DayFormatter(this);
        final String day = dayFormatter.format(weatherForecast.getTimestamp());
        tvDayName.setText(day);
        tvDayDesc.setText(weatherForecast.getDescription());
        tvDayMin.setText( TemperatureFormatter.format(weatherForecast.getMinimumTemperature()));
        tvDayMax.setText(TemperatureFormatter.format(weatherForecast.getMaximumTemperature()));
        tvDayHumidity.setText(""+weatherForecast.getHumidity());
        tvNight.setText(""+weatherForecast.getmNight());
        tvMorn.setText(""+weatherForecast.getmMorn());
        tvEven.setText(""+weatherForecast.getmEven());
        tvspeed.setText(""+weatherForecast.getSpeed());
        tvrain.setText(""+weatherForecast.getRain());
        tvpressure.setText(""+weatherForecast.getPressure());
        tvclouds.setText(""+weatherForecast.getClouds());
    }

    private void initViews() {
        tvDayName= (TextView) findViewById(R.id.tv_day_name);
        tvDayDesc= (TextView) findViewById(R.id.tv_day_desc);
        tvDayMin= (TextView) findViewById(R.id.tv_day_min);
        tvDayMax= (TextView) findViewById(R.id.tv_day_max);
        tvDayHumidity= (TextView) findViewById(R.id.tv_day_hum);
        tvNight=(TextView) findViewById(R.id.tv_day_night);
        tvMorn=(TextView) findViewById(R.id.tv_day_morn);
        tvEven=(TextView) findViewById(R.id.tv_day_even);
        tvclouds=(TextView) findViewById(R.id.tv_clouds);
        tvrain=(TextView) findViewById(R.id.tv_rain);
        tvpressure=(TextView) findViewById(R.id.tv_pressure);
        tvspeed=(TextView) findViewById(R.id.tv_speed);
    }

    private void callDataFromIntent() {
        Intent intent = getIntent();
        weatherForecast = (WeatherForecast) getIntent().getSerializableExtra("WEATHER_FORECAST");

    }
}
