package com.example.laundrysimply.model.response


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("address")
    val address: String,
    @SerializedName("created_at")
    val createdAt: Int,
    @SerializedName("current_team_id")
    val currentTeamId: Any,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("notelp")
    val notelp: String,
    @SerializedName("profile_photo_path")
    val profilePhotoPath: Any,
    @SerializedName("profile_photo_url")
    val profilePhotoUrl: String,
    @SerializedName("roles")
    val roles: String,
    @SerializedName("two_factor_confirmed_at")
    val twoFactorConfirmedAt: Any,
    @SerializedName("updated_at")
    val updatedAt: Int
)