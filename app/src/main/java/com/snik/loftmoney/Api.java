package com.snik.loftmoney;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @GET("auth")
    Call<AuthResult> auth(@Query("social_user_id") String socialUserId);

    @GET("items")
    Call<List<Item>> getItems(@Query("type") String type);

    @POST("items/add")
    Call<AddItemResult> addItem(@Query("price") int price, @Query("name") String name, @Query("type") String type);

    @POST("items/remove")
    Call<RemoveItemResult> removeItem(@Query("id") int id);

    @GET("balance")
    Call<BalanceResult> balance();
}
