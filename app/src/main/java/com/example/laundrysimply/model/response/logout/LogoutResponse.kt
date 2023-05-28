package com.example.laundrysimply.model.response.logout


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @Expose
    @SerializedName("data")
    val `data`: Boolean,
    @Expose
    @SerializedName("meta")
    val meta: Meta
)