package com.example.laundrysimply.model.response.detailtransaksi


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Pivot(
    @Expose
    @SerializedName("kuantitas")
    val kuantitas: String,
    @Expose
    @SerializedName("layanan_id")
    val layananId: Int,
    @Expose
    @SerializedName("transaksi_id")
    val transaksiId: Int
)