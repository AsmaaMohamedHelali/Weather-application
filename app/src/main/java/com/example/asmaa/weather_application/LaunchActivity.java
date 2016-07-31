package com.example.asmaa.weather_application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asmaa.weather_application.Adapter.CityListAdapter;
import com.example.asmaa.weather_application.Helpers.TemperatureFormatter;
import com.example.asmaa.weather_application.Models.CitiesModel;
import com.example.asmaa.weather_application.Models.CurrentWeather;
import com.example.asmaa.weather_application.Models.WeatherForecast;
import com.example.asmaa.weather_application.Services.CitiesService;
import com.example.asmaa.weather_application.Services.WeatherService;

import org.apache.http.HttpException;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by asmaa on 31/07/16.
 */

public class LaunchActivity extends Activity {
    private CompositeSubscription mCompositeSubscription;
    private RecyclerView recycler;
    private EditText edtCity;
    private Button searchBtb,GetLocationBtn;
    private LinearLayoutManager layoutManager;
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        mCompositeSubscription = new CompositeSubscription();
        initViews();
        recycler.setHasFixedSize(true);
         layoutManager= new LinearLayoutManager(getApplicationContext());


        searchBtb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pattern = edtCity.getEditableText().toString();
                CitiesService citiesService=new CitiesService();

                Observable fetchDataObservable =citiesService.fetchCities(pattern);

                mCompositeSubscription.add(fetchDataObservable
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<CitiesModel>>() {
                            @Override
                            public void onNext(final List<CitiesModel> weatherData) {
                                if(weatherData!=null){
                                    if(weatherData.size()>0){
                                        recycler.setLayoutManager(layoutManager);
                                        CityListAdapter adapter = new CityListAdapter(weatherData,
                                                LaunchActivity.this);

                                        recycler.setAdapter(adapter);
                                    }
                                }
                            }

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(final Throwable error) {

                                if (error instanceof TimeoutException) {
                                    Crouton.makeText(LaunchActivity.this,
                                            R.string.error_location_unavailable, Style.ALERT).show();
                                } else if (error instanceof RetrofitError
                                        || error instanceof HttpException) {
                                    Crouton.makeText(LaunchActivity.this,
                                            R.string.error_fetch_weather, Style.ALERT).show();
                                } else {
                                    Log.e("TAG", error.getMessage());
                                    error.printStackTrace();
                                    throw new RuntimeException("See inner exception");
                                }
                            }
                        })
                );
            }


        } );
        GetLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LaunchActivity.this, WeatherActivity.class);
                startActivity(i);


            }
        });


    }

    private void initViews() {
        edtCity = (EditText) findViewById(R.id.cityEdtText);
        recycler= (RecyclerView) findViewById(R.id.cityList);
        searchBtb= (Button) findViewById(R.id.buttonSearch);
        GetLocationBtn= (Button) findViewById(R.id.button);
    }

}
