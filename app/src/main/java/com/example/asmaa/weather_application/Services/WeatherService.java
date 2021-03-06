package com.example.asmaa.weather_application.Services;

import com.google.gson.annotations.SerializedName;

import org.apache.http.HttpException;

import java.util.ArrayList;
import java.util.List;

import com.example.asmaa.weather_application.Models.CurrentWeather;
import com.example.asmaa.weather_application.Models.WeatherForecast;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import rx.functions.Func1;

public class WeatherService {
    // We are implementing against version 2.5 of the Open Weather Map web service.
    private static final String WEB_SERVICE_BASE_URL = "http://api.openweathermap.org/data/2.5";
    private static final String API_KEY = "8f2e6e46906466ace5a2c39a189c5d3f";
    private final OpenWeatherMapWebService mWebService;

    public WeatherService() {
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

        mWebService = restAdapter.create(OpenWeatherMapWebService.class);
    }

    private interface OpenWeatherMapWebService {
        @GET("/weather?units=metric&apikey=" + API_KEY)
        Observable<CurrentWeatherDataEnvelope> fetchCurrentWeather(@Query("lon") double longitude,
                                                                   @Query("lat") double latitude);

        @GET("/forecast/daily?units=metric&cnt=7&apikey=" + API_KEY)
        Observable<WeatherForecastListDataEnvelope> fetchWeatherForecasts(
                @Query("lon") double longitude, @Query("lat") double latitude);
    }

    public Observable<CurrentWeather> fetchCurrentWeather(final double longitude,
                                                          final double latitude) {
        return mWebService.fetchCurrentWeather(longitude, latitude)
                .flatMap(new Func1<CurrentWeatherDataEnvelope,
                        Observable<? extends CurrentWeatherDataEnvelope>>() {

                    // Error out if the request was not successful.
                    @Override
                    public Observable<? extends CurrentWeatherDataEnvelope> call(
                            final CurrentWeatherDataEnvelope data) {
                        return data.filterWebServiceErrors();
                    }

                }).map(new Func1<CurrentWeatherDataEnvelope, CurrentWeather>() {

                    // Parse the result and build a CurrentWeather object.
                    @Override
                    public CurrentWeather call(final CurrentWeatherDataEnvelope data) {
                        return new CurrentWeather(data.locationName, data.timestamp,
                                data.weather.get(0).description, data.main.temp,
                                data.main.temp_min, data.main.temp_max);
                    }
                });
    }

    public Observable<List<WeatherForecast>> fetchWeatherForecasts(final double longitude,
                                                                   final double latitude) {
        return mWebService.fetchWeatherForecasts(longitude, latitude)
                .flatMap(new Func1<WeatherForecastListDataEnvelope,
                        Observable<? extends WeatherForecastListDataEnvelope>>() {

                    // Error out if the request was not successful.
                    @Override
                    public Observable<? extends WeatherForecastListDataEnvelope> call(
                            final WeatherForecastListDataEnvelope listData) {
                        return listData.filterWebServiceErrors();
                    }

                }).map(new Func1<WeatherForecastListDataEnvelope, List<WeatherForecast>>() {

                    // Parse the result and build a list of WeatherForecast objects.
                    @Override
                    public List<WeatherForecast> call(final WeatherForecastListDataEnvelope listData) {
                        final ArrayList<WeatherForecast> weatherForecasts =
                                new ArrayList<>();

                        for (WeatherForecastListDataEnvelope.ForecastDataEnvelope data : listData.list) {
                            final WeatherForecast weatherForecast = new WeatherForecast(
                                    listData.city.name, data.timestamp, data.weather.get(0).description,
                                    data.temp.min, data.temp.night,data.temp.eve,data.temp.morn,data.temp.max,
                                    data.speed,data.clouds,data.rain,data.pressure,data.humidity);
                            weatherForecasts.add(weatherForecast);
                        }

                        return weatherForecasts;


                    }
                });
    }

    /**
     * Base class for results returned by the weather web service.
     */
    private class WeatherDataEnvelope {
        @SerializedName("cod")
        private int httpCode;

        class Weather {
            public String description;
            private int id;
            private String main;
            private String icon;

        }

        /**
         * The web service always returns a HTTP header code of 200 and communicates errors
         * through a 'cod' field in the JSON payload of the response body.
         */
        public Observable filterWebServiceErrors() {
            if (httpCode == 200) {
                return Observable.just(this);
            } else {
                return Observable.error(
                        new HttpException("There was a problem fetching the weather data."));
            }
        }
    }

    /**
     * Data structure for current weather results returned by the web service.
     */
    private class CurrentWeatherDataEnvelope extends WeatherDataEnvelope {
        @SerializedName("name")
        public String locationName;
        @SerializedName("dt")
        public long timestamp;
        public ArrayList<Weather> weather;
        public Main main;






        private Coord coord;
        private String base;
        private Wind wind;
        private Clouds clouds;
        private Sys sys;

        class Main {
            public float temp;
            public float temp_min;
            public float temp_max;


            private Double pressure;
            private Integer humidity;
            private Double tempMin;
            private Double tempMax;
            private Double seaLevel;
            private Double grndLevel;
        }

        class Coord{
            private double lon;
            private double lat;
        }
        class Wind{
            private double speed;
            private double deg;

        }
        class Sys{

            private double message;
            private String country;
            private int sunrise;
            private int sunset;

        }
        class Clouds{
            private int all;

        }
    }

    /**
     * Data structure for weather forecast results returned by the web service.
     */
    private class WeatherForecastListDataEnvelope extends WeatherDataEnvelope {
        public Location city;
        public ArrayList<ForecastDataEnvelope> list;



        private double message;
        private int cnt;



        class Location {
            public String name;




            private int id;
            private Coord coord;
            private String country;
            private int population;

            class Coord {
                private double lon;
                private double lat;

            }
        }


        class ForecastDataEnvelope {
            @SerializedName("dt")
            public long timestamp;
            public Temperature temp;
            public ArrayList<Weather> weather;
            public double pressure;
            public double humidity;
            public float speed;
            public int clouds;
            public float rain;





            private Integer deg;

        }

        class Temperature {
            public float min;
            public float max;
            public float night;
            public float eve;
            public float morn;


        }
    }
}
