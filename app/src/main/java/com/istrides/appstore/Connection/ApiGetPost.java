package com.istrides.appstore.Connection;

import com.istrides.appstore.AppList;
import com.istrides.appstore.Model.AppDetailModel;
import com.istrides.appstore.Model.AppListModel;
import com.istrides.appstore.Model.LoginModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Mohankumar S on 6/18/2019$.
 */
public interface ApiGetPost {



    @POST("login")
    Call<LoginModel> Login(
            @Body ObjectBody.login login);

    @POST("app-list")
    Call<AppListModel> applist(
            @Body ObjectBody.applist list);

    @POST("app-details")
    Call<AppDetailModel> appdetails(
            @Body ObjectBody.appdetails list);


}
