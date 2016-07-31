package com.example.asmaa.weather_application.Services;

import com.example.asmaa.weather_application.Models.CitiesModel;
import com.example.asmaa.weather_application.Models.CurrentWeather;
import com.google.gson.annotations.SerializedName;

import org.apache.http.HttpException;

import java.util.ArrayList;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by asmaa on 31/07/16.
 */

public class CitiesService {
    private static final String WEB_SERVICE_BASE_URL = "http://api.openweathermap.org/data/2.5";
    private static final String API_KEY = "8f2e6e46906466ace5a2c39a189c5d3f";
    private final CitiesService.OpenWeatherMapWebService mWebService;

    public CitiesService() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(WEB_SERVICE_BASE_URL)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mWebService = restAdapter.create(CitiesService.OpenWeatherMapWebService.class);

    }
    private interface OpenWeatherMapWebService {
        @GET("/find?mode=json&type=like&units=metric&cnt=7&apikey=" + API_KEY)
        Observable<CitiesService.CitiesDataEnvelope> fetchCurrentWeather(@Query("q") String q);

    }
    public Observable<List<CitiesModel>> fetchCities( String q) {
        return mWebService.fetchCurrentWeather(q)
                .flatMap(new Func1<CitiesService.CitiesDataEnvelope,
                        Observable<? extends CitiesService.CitiesDataEnvelope>>() {

                    // Error out if the request was not successful.
                    @Override
                    public Observable<? extends CitiesService.CitiesDataEnvelope> call(
                            final CitiesService.CitiesDataEnvelope data) {
                        return data.filterWebServiceErrors();
                    }

                }).map(new Func1<CitiesService.CitiesDataEnvelope, List<CitiesModel>>() {

                    // Parse the result and build a CurrentWeather object.
                    @Override
                    public List<CitiesModel> call(final CitiesService.CitiesDataEnvelope data) {

                        final ArrayList<CitiesModel> weatherForecasts =
                                new ArrayList<>();

                        for (CitiesService.CitiesDataEnvelope.List Citydata : data.list) {
                            final CitiesModel weatherForecast = new CitiesModel(
                                    Citydata.getName(), Citydata.getCoord().lon,Citydata.getCoord().lat);
                            weatherForecasts.add(weatherForecast);
                        }

                        return weatherForecasts;

                    }
                });
    }

    private class CitiesDataEnvelope{
        private String message;
        @SerializedName("cod")
        private int httpCode;
        private int count;
        private java.util.List<List> list = new ArrayList<List>();

        /**
         *
         * @return
         *     The message
         */
        public String getMessage() {
            return message;
        }



        /**
         *
         * @return
         *     The count
         */
        public int getCount() {
            return count;
        }


        /**
         *
         * @return
         *     The list
         */
        public java.util.List<List> getList() {
            return list;
        }
        public Observable filterWebServiceErrors() {
            if (httpCode == 200) {
                return Observable.just(this);
            } else {
                return Observable.error(
                        new HttpException("There was a problem fetching the weather data."));
            }
        }
        class List{
            private int id;
            private String name;
            private Coord coord;
            private Main main;
            private int dt;
            private Wind wind;
            private Sys sys;
            private Clouds clouds;
            private java.util.List<Weather> weather = new ArrayList<Weather>();


            public String getName() {
                return name;
            }

            public Coord getCoord() {
                return coord;
            }
            class Coord {
                private double lon;
                private double lat;

            }
            class Main{
                private double temp;
                private float pressure;
                private float humidity;
                private double tempMin;
                private float tempMax;
            }
            class Wind{
                private double speed;
                private float deg;
            }
            class Sys{
                private String country;

            }
            class Clouds{
                private int all;


            }
            class Weather{
                private int id;
                private String main;
                private String description;
                private String icon;
            }
        }


    }


}
