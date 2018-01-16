package com.hyperpakhsh.sadeq.bazaartracker.Utils;



import com.hyperpakhsh.sadeq.bazaartracker.Map.LocationItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("address/getRegions")
    Call<ArrayList<NameValueItem>> getRegions(
    );

    @POST("customers/setCustomers")
    @FormUrlEncoded
    Call<String> setCustomer(
      @Field("customer") String jsonCustomer
    );


    //first insert new customer location then return its id
    @POST("address/getLocationId")
    @FormUrlEncoded
    Call<String> getLocationId(
      @Field("latlong") String latlong,
      @Field("address") String address
    );

    @POST("address/getLocations")
    Call<ArrayList<LocationItem>> getLocations();

    @POST("customers/login")
    @FormUrlEncoded
    Call<ArrayList<NameValueItem>> login(
      @Field("username") String username,
      @Field("password") String password
    );

}
