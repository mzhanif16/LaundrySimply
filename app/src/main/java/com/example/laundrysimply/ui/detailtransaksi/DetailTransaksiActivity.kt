package com.example.laundrysimply.ui.detailtransaksi

import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundrysimply.MainActivity
import com.example.laundrysimply.R
import com.example.laundrysimply.databinding.ActivityDetailTransaksiBinding
import com.example.laundrysimply.model.response.detailtransaksi.DetailTransaksiResponse
import com.example.laundrysimply.model.response.detailtransaksi.Layanan
import com.example.laundrysimply.model.response.detailtransaksi.Pivot
import com.example.laundrysimply.model.response.transaksi.UpdateTransaksiResponse
import com.example.laundrysimply.utils.Helpers.formatPrice

class DetailTransaksiActivity : AppCompatActivity(), DetailTransaksiContract.View,
    UpdateTransaksiContract.View {
    var progressDialog: Dialog? = null
    lateinit var presenter: DetailTransaksiPresenter
    lateinit var presenter2: UpdateTransaksiPresenter
    private lateinit var binding: ActivityDetailTransaksiBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tvPaymentURL = binding.tvPaymentURL
        tvPaymentURL.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        val transaksiId = intent.getIntExtra("transaksi_id", 0)
        initView()
        presenter = DetailTransaksiPresenter(this)
        presenter2 = UpdateTransaksiPresenter(this)
        presenter.getDetailTransaksi(transaksiId)

        val rating = binding.rbOutlet
        rating.setIsIndicator(false)


        binding.btnLanjut.setOnClickListener {
            val keterangan = binding.etKeterangan.text.toString()
            val nilaiRating = rating.rating
            if(keterangan.isNullOrEmpty()){
                binding.etKeterangan.error ="Silahkan masukkan keterangan"
                binding.etKeterangan.requestFocus()
            }else if(nilaiRating== 0.0f){
                Toast.makeText(this,"Rating harus diisi",Toast.LENGTH_SHORT).show()
            }else {
                presenter2.Rating(transaksiId, nilaiRating, keterangan)
            }
        }

        binding.btnCancel.setOnClickListener {
            binding.tvPaymentURL.text = "-"
            presenter2.Cancel(transaksiId,"CANCELLED")
        }

        val ivback = binding.imageView4
        ivback.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
    private fun initView() {
        progressDialog = Dialog(this)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_loader, null)

        progressDialog?.let {
            it.setContentView(dialogLayout)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun onTransaksiSuccess(detailTransaksiResponse: DetailTransaksiResponse) {
        val data = detailTransaksiResponse.layanan
        val listData = ArrayList<Layanan>(data)

        binding.btnLanjut.visibility = if (detailTransaksiResponse.statusTransaksi == "DELIVERED") {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.btnCancel.visibility = if(detailTransaksiResponse.statusBayar == "SUCCESS" || detailTransaksiResponse.statusTransaksi == "CANCELLED"){
            View.GONE
        }else {
            View.VISIBLE
        }
        binding.cvRating.visibility = if (detailTransaksiResponse.statusTransaksi == "DELIVERED") {
            View.VISIBLE
        } else {
            View.GONE
        }


        var adapter = DetailTransaksiAdapter(listData)
        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.rvDetailTransaksi.layoutManager = layoutManager
        binding.rvDetailTransaksi.adapter = adapter
        binding.tvNamaoutlet.text = detailTransaksiResponse.outlet.nama
        binding.tvIdtransaksi.text = detailTransaksiResponse.id.toString()
        binding.tvNamauser.text = detailTransaksiResponse.user.name
        binding.tvNotelp.text = detailTransaksiResponse.user.notelp
        binding.tvAlamat.text = detailTransaksiResponse.user.address
        binding.tvPaymentURL.text = detailTransaksiResponse.paymentUrl
        if(detailTransaksiResponse.statusTransaksi.equals("CANCELLED")){
            binding.tvPaymentURL.visibility = View.GONE
        }
        binding.tvTotalBayar.formatPrice(detailTransaksiResponse.totalBayar)
        binding.tvTotalkuantitas.text = detailTransaksiResponse.kuantitas
        binding.tvStatusBayar.text = detailTransaksiResponse.statusBayar
        binding.tvStatusTransaksi.text = detailTransaksiResponse.statusTransaksi
        if(detailTransaksiResponse.statusTransaksi.equals("CANCELLED")){
            binding.tvStatusTransaksi.setBackgroundResource(R.drawable.shape_status_cancelled)
        }else if (detailTransaksiResponse.statusTransaksi.equals("DELIVERED")){
            binding.tvStatusTransaksi.setBackgroundResource(R.drawable.shape_status_delivered)
        }
        binding.tvWaktuanter.text = detailTransaksiResponse.waktuPengantaran
        binding.tvWaktupesan.text = detailTransaksiResponse.waktuPemesanan
        binding.rbOutlet.rating = detailTransaksiResponse.rating
        binding.etKeterangan.setText(detailTransaksiResponse.keterangan)
        if(detailTransaksiResponse.keterangan.isNullOrEmpty()){
            binding.etKeterangan.isEnabled = true
            binding.rbOutlet.setIsIndicator(false)
//            binding.btnLanjut.visibility = View.VISIBLE
        }else{
            binding.etKeterangan.isEnabled = false
            binding.rbOutlet.setIsIndicator(true)
            binding.btnLanjut.visibility = View.GONE
        }

        binding.tvPaymentURL.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(detailTransaksiResponse.paymentUrl)
            startActivity(i)
        }
        Toast.makeText(this, "Sukses Mengambil Data Transaksi", Toast.LENGTH_SHORT).show()
    }

    override fun onTransaksiFailed(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onUpdateTransaksiSuccess(updateTransaksiPresenter: UpdateTransaksiResponse) {
        Toast.makeText(this, "Sukses Update Transaksi", Toast.LENGTH_SHORT).show()
        val pindah = Intent(this, MainActivity::class.java)
        startActivity(pindah)
    }

    override fun onUpdateTransaksiFailed(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.dismiss()
    }
}