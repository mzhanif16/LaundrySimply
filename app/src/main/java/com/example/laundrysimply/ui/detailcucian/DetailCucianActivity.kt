package com.example.laundrysimply.ui.detailcucian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundrysimply.LaundrySimply
import com.example.laundrysimply.databinding.ActivityDetailCucianBinding
import com.example.laundrysimply.model.response.layanan.Data
import com.example.laundrysimply.model.response.login.User
import com.example.laundrysimply.utils.Helpers.formatPrice
import com.google.gson.Gson

class DetailCucianActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCucianBinding
    private var totalKuantitas: Int = 0
    private var totalBayar: Int = 0
    private var outletNama: String = ""
    private var outletAlamat: String = ""
    private lateinit var dataA: ArrayList<Data>
    private var tanggalPickup: String = ""
    private var tanggalDelivery: String = ""
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
        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        binding.btnLanjut.setOnClickListener {

        }


        binding.cvRating.visibility = View.INVISIBLE
        binding.cardView.visibility = View.INVISIBLE
    }
}