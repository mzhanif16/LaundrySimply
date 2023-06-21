package com.example.laundrysimply.network


import android.text.Editable
import com.example.laundrysimply.model.response.Wrapper
import com.example.laundrysimply.model.response.checkout.CheckOutResponse
import com.example.laundrysimply.model.response.detailtransaksi.DetailTransaksiResponse
import com.example.laundrysimply.model.response.home.HomeResponse
import com.example.laundrysimply.model.response.layanan.LayananResponse
import com.example.laundrysimply.model.response.login.LoginResponse
import com.example.laundrysimply.model.response.logout.LogoutResponse
import com.example.laundrysimply.model.response.profile.UpdateProfileResponse
import com.example.laundrysimply.model.response.transaksi.TransaksiResponse
import com.example.laundrysimply.model.response.transaksi.UpdateTransaksiResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface Endpoint {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Observable<Wrapper<LoginResponse>>

    @FormUrlEncoded
    @POST("user")
    fun UpdateProfile(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("notelp") notelp: String,
        @Field("address") password: String
    ): Observable<Wrapper<UpdateProfileResponse>>

    @FormUrlEncoded
    @POST("checkout")
    fun checkout(
        @Field("layanan_id") layanan_id: String,
        @Field("user_id") user_id: Int,
        @Field("total_bayar") total_bayar: String,
        @Field("status_transaksi") status_transaksi: String,
        @Field("status_bayar") status_bayar: String,
        @Field("kuantitas") kuantitas: String,
        @Field("waktu_pemesanan") waktu_pemesanan: String,
        @Field("waktu_pengantaran") waktu_pengantaran: String,
        @Field("rating") rating: Int,
        @Field("keterangan") keterangan: String
    ): Observable<Wrapper<CheckOutResponse>>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String,
        @Field("address") address: String,
        @Field("notelp") notelp: String
    ): Observable<Wrapper<LoginResponse>>

    @Multipart
    @POST("user/photo")
    fun registerPhoto(@Part profileImage: MultipartBody.Part): Observable<Wrapper<Any>>

    @GET("outlet")
    fun home(): Observable<Wrapper<HomeResponse>>

    @GET("layanan")
    fun layanan(): Observable<Wrapper<LayananResponse>>

    @GET("transaksi")
    fun transaksi(): Observable<Wrapper<TransaksiResponse>>

    @GET("transaksidetail/{id}")
    fun detailtransaksi(@Path("id") id: Int): Observable<Wrapper<DetailTransaksiResponse>>

    @FormUrlEncoded
    @POST("transaksi/{id}")
    fun updateRating(
        @Path("id") id: Int,
        @Field("rating") rating: Float,
        @Field("keterangan") keterangan: String
    ): Observable<Wrapper<UpdateTransaksiResponse>>

    @POST("logout")
    fun logout(@Header("Authorization") accessToken: String): Observable<LogoutResponse>
}