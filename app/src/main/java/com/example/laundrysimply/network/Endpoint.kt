package com.example.laundrysimply.network


import com.example.laundrysimply.model.response.Wrapper
import com.example.laundrysimply.model.response.home.HomeResponse
import com.example.laundrysimply.model.response.layanan.LayananResponse
import com.example.laundrysimply.model.response.login.LoginResponse
import com.example.laundrysimply.model.response.logout.LogoutResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface Endpoint {

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("email")email:String,
              @Field("password")password:String) : Observable<Wrapper<LoginResponse>>

    @FormUrlEncoded
    @POST("register")
    fun register(@Field("name")name:String,
                 @Field("email")email:String,
                 @Field("password")password:String,
                 @Field("password_confirmation")password_confirmation:String,
                 @Field("address")address:String,
                 @Field("notelp")notelp:String) : Observable<Wrapper<LoginResponse>>

    @Multipart
    @POST("user/photo")
    fun registerPhoto(@Part profileImage:MultipartBody.Part) : Observable<Wrapper<Any>>

    @GET("outlet")
    fun home() : Observable<Wrapper<HomeResponse>>

    @GET("layanan")
    fun layanan() : Observable<Wrapper<LayananResponse>>

    @POST("logout")
    fun logout(@Header("Authorization")accessToken: String): Observable<LogoutResponse>
}