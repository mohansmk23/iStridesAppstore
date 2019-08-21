package com.istrides.appstore.Connection;

import com.istrides.appstore.AppList;
import com.istrides.appstore.Model.AppDetailModel;
import com.istrides.appstore.Model.AppListModel;
import com.istrides.appstore.Model.LoginModel;
import com.istrides.appstore.Model.ProfileModel;
import com.istrides.appstore.Model.SuccessModel;

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

    @POST("my-profile")
    Call<ProfileModel> getprofile(
            @Body ObjectBody.applist list);

    @POST("profile-update")
    Call<SuccessModel> changename(
            @Body ObjectBody.changename list);


    @POST("api_change-password")
    Call<SuccessModel> changepass(
            @Body ObjectBody.changepass list);


}
