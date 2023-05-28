package com.example.laundrysimply.ui.layanan

import LayananAdapter
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundrysimply.PilihWaktuActivity
import com.example.laundrysimply.R
import com.example.laundrysimply.databinding.ActivityDetailLayananBinding
import com.example.laundrysimply.model.response.layanan.Data
import com.example.laundrysimply.model.response.layanan.LayananResponse
import com.example.laundrysimply.utils.Helpers.formatPrice

class DetailLayananActivity : AppCompatActivity(), LayananAdapter.ItemAdapterCallback,
    LayananContract.View {
    private var layananList : ArrayList<Data> = ArrayList()
    private lateinit var binding: ActivityDetailLayananBinding
    private lateinit var presenter: LayananPresenter
    var progressDialog : Dialog? = null
    private var outletId: Int =0
    private var totalKuantitas: Int = 0
    private var totalBayar: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLayananBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initView()
        outletId = intent.getIntExtra("outlet_id",0)
        presenter = LayananPresenter(this)
        presenter.getLayanan(outletId)

        binding.btnLanjut.setOnClickListener {
            val intent = Intent(this, PilihWaktuActivity::class.java)
            startActivity(intent)
        }
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

    override fun onClick(v: View, data: Data) {

    }

    override fun onLayananSuccess(layananResponse: LayananResponse) {
        val filteredLayanan = layananResponse.data.filter { it.outletId == outletId }
        layananList.addAll(filteredLayanan)
        var adapter = LayananAdapter(
            layananList,
            this,
            outletId,
            this::onTotalKuantitasUpdated,
            this::onTotalBayarUpdated)
        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        binding.rvLayanan.layoutManager= layoutManager
        binding.rvLayanan.adapter = adapter
    }

    override fun onLayananFailed(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.dismiss()
    }
    private fun onTotalKuantitasUpdated(total: Int) {
        totalKuantitas = total
        binding.tvTotalKuantitas.text = total.toString()
    }

    private fun onTotalBayarUpdated(total: Int) {
        totalBayar = total
        binding.tvTotalBayar2.formatPrice(total.toString())
    }
}