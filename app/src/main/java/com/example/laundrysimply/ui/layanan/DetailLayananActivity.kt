package com.example.laundrysimply.ui.layanan

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
import com.mcdev.quantitizerlibrary.HorizontalQuantitizer
import com.mcdev.quantitizerlibrary.QuantitizerListener

class DetailLayananActivity : AppCompatActivity(), LayananAdapter.ItemAdapterCallback,
    LayananContract.View {
    private var layananList : ArrayList<Data> = ArrayList()
    private lateinit var binding: ActivityDetailLayananBinding
    private lateinit var presenter: LayananPresenter
    var progressDialog : Dialog? = null
    private var outletId: Int =0
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

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)

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

//    fun initDataDummy(){
//        layananList = ArrayList()
//        layananList.add(LayananModel("Cuci Setrika","9500"))
//        layananList.add(LayananModel("Cuci","8000"))
//        layananList.add(LayananModel("Cuci Kering","7500"))
//
//    }

    override fun onClick(v: View, data: Data) {

    }

    override fun onLayananSuccess(layananResponse: LayananResponse) {
        val filteredLayanan = layananResponse.data.filter { it.outletId == outletId }
        var adapter = LayananAdapter(filteredLayanan,this, outletId)
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
}