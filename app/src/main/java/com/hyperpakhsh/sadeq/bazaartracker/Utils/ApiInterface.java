package com.hyperpakhsh.sadeq.bazaartracker.Utils;



import com.hyperpakhsh.sadeq.bazaartracker.Customers.CustomerListItem;
import com.hyperpakhsh.sadeq.bazaartracker.Login.InitItem;
import com.hyperpakhsh.sadeq.bazaartracker.Order.ProductItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {


    @POST("customers/getCustomers")
    Call<ArrayList<CustomerListItem>> getCustomers(
    );


    @POST("customers/login")
    @FormUrlEncoded
    Call<ArrayList<NameValueItem>> login(
      @Field("username") String username,
      @Field("password") String password
    );


    @POST("products/getProducts")
    Call<ArrayList<ProductItem>> getProducts();

    @POST("orders/addOrder")
    @FormUrlEncoded
    Call<String> addOrder(
        @Field("order") String order
    );

    @POST("orders/addFactor")
    @FormUrlEncoded
    Call<String> addFactor(
            @Field("factor") String factorJson
    );

    @POST("products/addProduct")
    @FormUrlEncoded
    Call<String> addProduct(
            @Field("name") String productName,
            @Field("price") int price
    );

    @POST("customers/getInitialValues")
    Call<ArrayList<InitItem>> getInits();

    @POST("customers/addCustomer")
    @FormUrlEncoded
    Call<String> addCustomer(
            @Field("name") String customer,
            @Field("phone") String phone
    );


}
