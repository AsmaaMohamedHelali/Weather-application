package com.example.asmaa.weather_application.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asmaa.weather_application.LaunchActivity;
import com.example.asmaa.weather_application.Models.CitiesModel;
import com.example.asmaa.weather_application.R;
import com.example.asmaa.weather_application.WeatherActivity;


/**
 * Created by asmaa on 31/07/16.
 */

public class CityListAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    private java.util.List<CitiesModel> list2;
    private RecyclerView.ViewHolder holder;
    private Activity activity;


    View v;

    public CityListAdapter(java.util.List<CitiesModel> employeeList,Activity activity){

        list2 = employeeList;
        this.activity=activity;


    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_city_row, parent, false);

        holder = new SearchResultViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchResultViewHolder) {
            ((SearchResultViewHolder) holder).activity=activity;

            ((SearchResultViewHolder) holder).tv_book_name.setText(list2.get(position).getmCityName());
            ((SearchResultViewHolder) holder).tv_emp_name.setText(list2.get(position).getLongg()+","+
                    list2.get(position).getLatt());
            ((SearchResultViewHolder) holder).CoordLong=list2.get(position).getLongg();
            ((SearchResultViewHolder) holder).CoordLatt=list2.get(position).getLatt();

        }
    }

    @Override
    public int getItemCount() {
        if(list2!=null)
            return list2.size();
        else
            return 0;
    }

    @Override
    public void onClick(View view) {


    }


    public static class SearchResultViewHolder extends RecyclerView.ViewHolder  {
        public TextView tv_book_name,tv_emp_name;
        public Activity activity;
        public double CoordLong,CoordLatt;


        public SearchResultViewHolder(View v) {
            super(v);
            tv_book_name = (TextView) v.findViewById(R.id.tv_book_name);
            tv_emp_name = (TextView) v.findViewById(R.id.tv_emp_name);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(activity, WeatherActivity.class);
                    i.putExtra("CoordLong", CoordLong);
                    i.putExtra("CoordLatt", CoordLatt);
                    activity.startActivity(i);
                }
            });

        }






    }

}
