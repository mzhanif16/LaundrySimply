package com.example.laundrysimply.model.response.layanan


import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    @Expose
    @SerializedName("created_at")
    val createdAt: Int,
    @Expose
    @SerializedName("deleted_at")
    val deletedAt: String?,
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
    val updatedAt: Int,

    var kuantitas: Int? =0
) : Parcelable