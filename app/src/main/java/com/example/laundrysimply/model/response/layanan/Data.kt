package com.example.laundrysimply.model.response.layanan


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
    @Expose
    @SerializedName("created_at")
    val createdAt: Int,
    @Expose
    @SerializedName("deleted_at")
    val deletedAt: Any,
    @Expose
    @SerializedName("deskripsi")
    val deskripsi: String,
    @Expose
    @SerializedName("harga")
    val harga: String,
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("nama_layanan")
    val namaLayanan: String,
    @Expose
    @SerializedName("outlet_id")
    val outletId: Int,
    @Expose
    @SerializedName("updated_at")
    val updatedAt: Int
)