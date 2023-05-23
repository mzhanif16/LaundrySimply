package com.example.laundrysimply.model.response.home


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
    @Expose
    @SerializedName("address")
    val address: String,
    @Expose
    @SerializedName("created_at")
    val createdAt: Int,
    @Expose
    @SerializedName("deleted_at")
    val deletedAt: Any,
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("keterangan")
    val keterangan: String,
    @Expose
    @SerializedName("layanan_id")
    val layananId: Int,
    @Expose
    @SerializedName("nama")
    val nama: String,
    @Expose
    @SerializedName("rating")
    val rating: Int,
    @Expose
    @SerializedName("updated_at")
    val updatedAt: Int
)