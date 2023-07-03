package com.example.laundrysimply.ui.detailcucian

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundrysimply.LaundrySimply
import com.example.laundrysimply.MainActivity
import com.example.laundrysimply.ONESIGNAL_APP_ID
import com.example.laundrysimply.R
import com.example.laundrysimply.databinding.ActivityDetailCucianBinding
import com.example.laundrysimply.model.response.checkout.CheckOutResponse1
import com.example.laundrysimply.model.response.layanan.Data
import com.example.laundrysimply.model.response.login.User
import com.example.laundrysimply.utils.Helpers.formatPrice
import com.google.gson.Gson
import com.onesignal.OneSignal
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class DetailCucianActivity : AppCompatActivity(), PaymentContract.View {
    private lateinit var binding: ActivityDetailCucianBinding
    private var totalKuantitas: Int = 0
    private var totalBayar: Int = 0
    private var outletNama: String = ""
    private var outletId: Int = 0
    private var outletAlamat: String = ""
    private lateinit var dataA: ArrayList<Data>
    private var tanggalPickup: String = ""
    private var tanggalDelivery: String = ""
    lateinit var presenter: PaymentPresenter
    var progressDialog : Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCucianBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var user = LaundrySimply.getApp().getUser()
        var userResponse = Gson().fromJson(user, User::class.java)

        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        OneSignal.promptForPushNotifications();

        val bundle = intent.extras
        if (bundle != null) {
            totalKuantitas = bundle.getInt("total_kuantitas", 0)
            totalBayar = bundle.getInt("total_bayar", 0)
            outletId = bundle.getInt("outlet_id", 0)
            outletNama = bundle.getString("outlet_nama", "").toString()
            outletAlamat = bundle.getString("outlet_alamat", "").toString()
            dataA = bundle.getParcelableArrayList<Data>("data") as ArrayList<Data>
            tanggalPickup = bundle.getString("tanggal_pickup", "").toString()
            tanggalDelivery = bundle.getString("tanggal_delivery", "").toString()
        }

        Log.d("datanye", dataA.toString())

        binding.tvNamaoutlet.text = outletNama
        binding.tvNamauser.text = userResponse.name
        binding.tvAlamat.text = userResponse.address.toString()
        binding.tvNotelp.text = userResponse.notelp.toString()
        binding.tvWaktupesan.text = tanggalPickup
        binding.tvWaktuanter.text = tanggalDelivery
        binding.tvTotalBayar.formatPrice(totalBayar.toString())

        val adapter = DetailCucianAdapter(dataA)
        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        initView()
        presenter = PaymentPresenter(this)
        val kuantitasperlayanan = dataA.map { data -> data.kuantitas  }
        val result = kuantitasperlayanan.joinToString(separator = "")
        Log.d("kuantitas", result.toString())
        val dataIds = dataA.map { data -> data.id }
        val dataIdsString = dataIds.joinToString(",")
        val dataArray = dataIdsString
            .replace("[", "") // Menghapus karakter "[" dari string
            .replace("]", "") // Menghapus karakter "]" dari string
            .split(",") // Memisahkan string berdasarkan koma
            .map { it.trim().toInt() }

        binding.btnLanjut.setOnClickListener {
            presenter.CheckOut(
                dataArray,
                userResponse.id,
                totalBayar.toString(),
                result,
                tanggalPickup,
                tanggalDelivery,
                0,
                "",
                outletId,
                it
            )
        }
    }

    private fun initView(){
        progressDialog = Dialog(this)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_loader, null)

        progressDialog?.let {
            it.setContentView(dialogLayout)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    fun onbackPressed(view: View) {
        super.onBackPressed()
    }

    override fun onPaymentSuccess(checkOutResponse: CheckOutResponse1, view: View) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.logo)
        alertDialog.setTitle("Pemberitahuan")
        alertDialog.setMessage("Apakah Anda ingin membayar sekarang?")
        alertDialog.setPositiveButton("Ya") { dialog, which ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(checkOutResponse.paymentUrl)
            startActivity(intent)
        }
        alertDialog.setNegativeButton("Tidak") { dialog, which ->
            // Intent ke Home Fragment
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragment", "home")
            startActivity(intent)
            finish()
        }
        alertDialog.show()
        val playerID = OneSignal.getDeviceState()?.userId
        val notificationContent = JSONObject()
        try {
            notificationContent.put("app_id", ONESIGNAL_APP_ID)
            notificationContent.put("include_player_ids", JSONArray().put(playerID))
            notificationContent.put("contents", JSONObject().put("en", "Cucian Anda sedang diproses."))
            notificationContent.put("headings", JSONObject().put("en", "Cucian Diproses"))
            notificationContent.put("android_small_icon", R.drawable.logo)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val url = "https://onesignal.com/api/v1/notifications"
        val client = OkHttpClient()

        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), notificationContent.toString())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Authorization", "MWMzZjM2NzUtMDVhNS00OGU4LWIyMzgtMmRiYTZiM2U4YmY5")
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                response.isSuccessful
            }

            override fun onFailure(call: Call, e: IOException) {
                e.message
            }
        })
    }

    override fun onPaymentFailed(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.dismiss()
    }
}