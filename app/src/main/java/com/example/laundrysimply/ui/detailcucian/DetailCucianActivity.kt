package com.example.laundrysimply.ui.detailcucian

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundrysimply.LaundrySimply
import com.example.laundrysimply.MainActivity
import com.example.laundrysimply.R
import com.example.laundrysimply.databinding.ActivityDetailCucianBinding
import com.example.laundrysimply.model.response.checkout.CheckOutResponse
import com.example.laundrysimply.model.response.layanan.Data
import com.example.laundrysimply.model.response.login.User
import com.example.laundrysimply.utils.Helpers.formatPrice
import com.google.gson.Gson

class DetailCucianActivity : AppCompatActivity(), PaymentContract.View {
    private lateinit var binding: ActivityDetailCucianBinding
    private var totalKuantitas: Int = 0
    private var totalBayar: Int = 0
    private var outletNama: String = ""
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

        val bundle = intent.extras
        if (bundle != null) {
            totalKuantitas = bundle.getInt("total_kuantitas", 0)
            totalBayar = bundle.getInt("total_bayar", 0)
            outletNama = bundle.getString("outlet_nama", "").toString()
            outletAlamat = bundle.getString("outlet_alamat", "").toString()
            dataA = bundle.getParcelableArrayList<Data>("data") as ArrayList<Data>
            tanggalPickup = bundle.getString("tanggal_pickup", "").toString()
            tanggalDelivery = bundle.getString("tanggal_delivery", "").toString()
        }

        var user = LaundrySimply.getApp().getUser()
        var userResponse = Gson().fromJson(user, User::class.java)
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
        val dataIds = dataA.map { data -> data.id }
        val dataIdsString = dataIds.joinToString(",")
//        val gson = Gson()
//        val dataListA = gson.toJson(dataA)
//
//        val dataListA = JSONArray()
//        for (data in dataA) {
//            val jsonArray = JSONArray()
//            jsonArray.put(data.id)
//            jsonArray.put(data.namaLayanan)
//            jsonArray.put(data.outletId)
//            jsonArray.put(data.kuantitas)
//            dataListA.put(jsonArray)
//        }

        binding.btnLanjut.setOnClickListener {
            presenter.CheckOut(
                dataIdsString,
                userResponse.id,
                totalBayar.toString(),
                totalKuantitas.toString(),
                tanggalPickup,
                tanggalDelivery,
                5,
                "",
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

    override fun onPaymentSuccess(checkOutResponse: CheckOutResponse, view: View) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.ic_launcher_foreground)
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