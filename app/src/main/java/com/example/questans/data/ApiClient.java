package com.example.questans.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String port="8000";
//    private static String url = "http://10.0.2.2:8080/api/";

//    private static final String ip="172.16.63.61";
    private static final String ip="192.168.1.6";
//    private static final String ip="192.168.1.2";
    private static final String url = "http://"+ip+":"+port+"/api/";

//    private static String url = "https://learn4u.herokuapp.com/api/";
    private static Retrofit retrofit;
    public static ApiInterface getAPI() {
        if (retrofit == null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }
}