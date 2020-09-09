package com.niagait.silapopt.view.callback;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginRetrofitCallBack {
    @GET("Login/")
    Call<ResponseBody> login(
            @Query("username") String username,
            @Query("password") String password,
            @Query("token_firebase") String token_firebase,
            @Query("fbclid") String fbclid
    );

    @GET("SlideAndroid")
    Call<ResponseBody> slide();
}
