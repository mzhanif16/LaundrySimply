package com.example.laundrysimply.layanan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundrysimply.PilihWaktuActivity
import com.example.laundrysimply.databinding.ActivityDetailLayananBinding
import com.example.laundrysimply.model.dummy.LayananModel

class DetailLayananActivity : AppCompatActivity(), LayananAdapter.ItemAdapterCallback{
    private var layananList : ArrayList<LayananModel> = ArrayList()
    private lateinit var binding: ActivityDetailLayananBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLayananBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLanjut.setOnClickListener {
            val intent = Intent(this, PilihWaktuActivity::class.java)
            startActivity(intent)
        }
        initDataDummy()
        var adapter = LayananAdapter(layananList,this)
        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        binding.rvLayanan.layoutManager= layoutManager
        binding.rvLayanan.adapter = adapter
    }
    fun initDataDummy(){
        layananList = ArrayList()
        layananList.add(LayananModel("Cuci Setrika","9500"))
        layananList.add(LayananModel("Cuci","8000"))
        layananList.add(LayananModel("Cuci Kering","7500"))

    }

    override fun onClick(v: View, data: LayananModel) {

    }
}