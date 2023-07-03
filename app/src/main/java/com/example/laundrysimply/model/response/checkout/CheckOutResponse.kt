package com.example.laundrysimply.model.response.checkout


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckOutResponse(
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
    @SerializedName("kuantitas")
    val kuantitas: String,
    @Expose
    @SerializedName("layanan_id")
    val layananId: String,
    @Expose
    @SerializedName("outlet")
    val outlet: Outlet,
    @Expose
    @SerializedName("outlet_id")
    val outletId: Int,
    @Expose
    @SerializedName("payment_url")
    val paymentUrl: String,
    @Expose
    @SerializedName("rating")
    val rating: Int,
    @Expose
    @SerializedName("status_bayar")
    val statusBayar: String,
    @Expose
    @SerializedName("status_transaksi")
    val statusTransaksi: String,
    @Expose
    @SerializedName("total_bayar")
    val totalBayar: String,
    @Expose
    @SerializedName("updated_at")
    val updatedAt: Int,
    @Expose
    @SerializedName("user")
    val user: User,
    @Expose
    @SerializedName("user_id")
    val userId: Int,
    @Expose
    @SerializedName("waktu_pemesanan")
    val waktuPemesanan: String,
    @Expose
    @SerializedName("waktu_pengantaran")
    val waktuPengantaran: String
)