package com.example.laundrysimply.model.response.checkout


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Outlet(
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
    @SerializedName("nama")
    val nama: String,
    @Expose
    @SerializedName("updated_at")
    val updatedAt: Int
)