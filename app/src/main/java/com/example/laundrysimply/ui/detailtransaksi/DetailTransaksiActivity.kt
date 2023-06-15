package com.example.laundrysimply.ui.detailtransaksi

import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.laundrysimply.R
import com.example.laundrysimply.databinding.ActivityDetailCucianBinding
import com.example.laundrysimply.databinding.ActivityDetailTransaksiBinding
import com.example.laundrysimply.model.response.detailtransaksi.DetailTransaksiResponse
import com.example.laundrysimply.model.response.transaksi.TransaksiResponse
import com.example.laundrysimply.ui.cucian.TransaksiContract
import com.example.laundrysimply.ui.cucian.TransaksiPresenter

class DetailTransaksiActivity : AppCompatActivity() , DetailTransaksiContract.View{
    var progressDialog : Dialog? = null
    lateinit var presenter: DetailTransaksiPresenter
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
        presenter.getDetailTransaksi(transaksiId)


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

    override fun onTransaksiSuccess(detailTransaksiResponse: DetailTransaksiResponse) {
        binding.tvIdtransaksi.text = detailTransaksiResponse.id.toString()
        binding.tvNamauser.text= detailTransaksiResponse.user.name
        binding.tvNotelp.text = detailTransaksiResponse.user.notelp
        binding.tvAlamat.text = detailTransaksiResponse.user.address
        binding.tvPaymentURL.text = detailTransaksiResponse.paymentUrl
        binding.tvTotalBayar.text = detailTransaksiResponse.totalBayar
        binding.tvStatusBayar.text = detailTransaksiResponse.statusBayar
        binding.tvStatusTransaksi.text = detailTransaksiResponse.statusTransaksi
        binding.tvWaktuanter.text = detailTransaksiResponse.waktuPengantaran
        binding.tvWaktupesan.text = detailTransaksiResponse.waktuPemesanan

        binding.tvPaymentURL.setOnClickListener {
            val i= Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(detailTransaksiResponse.paymentUrl)
            startActivity(i)
        }
        Toast.makeText(this, "Sukses Mengambil Data Transaksi", Toast.LENGTH_SHORT).show()
    }

    override fun onTransaksiFailed(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.dismiss()
    }
}