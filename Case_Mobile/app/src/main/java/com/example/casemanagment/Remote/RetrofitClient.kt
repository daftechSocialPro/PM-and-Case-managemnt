package com.example.casemanagment.Remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetrofitClient {

    private var instance: Retrofit?= null

    fun getInstance (ipaddress:String?, pubname:String?,port :String?): Retrofit{

        var url = "";
        if (!pubname.isNullOrEmpty()){


            url ="http://$ipaddress:$port/$pubname/api/MobileUser/";
        }
        else {
            url =  "http://$ipaddress:$port/api/MobileUser/";
        }


        if (instance==null) {

            val gson = GsonBuilder()
                .setLenient()
                .create();

            instance = Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        }

        return instance!!

    }
}