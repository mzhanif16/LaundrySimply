package com.example.laundrysimply.ui.detailtransaksi

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundrysimply.R
import com.example.laundrysimply.model.response.layanan.Data

class DetailTransaksiAdapter(
    private val dataAList: ArrayList<Data>
    ) :
    RecyclerView.Adapter<DetailTransaksiAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_layanan_transaksi, parent, false)
        return DataViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataAList.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentItem = dataAList[position]
        holder.bind(currentItem)
    }


    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNamaLayanan: TextView = itemView.findViewById(R.id.tv_nama_layanan)
        private val tvHargaLayanan: TextView = itemView.findViewById(R.id.tv_harga_layanan)
        fun bind(data: Data) {
            itemView.apply {
                tvNamaLayanan.text = data.namaLayanan
                tvHargaLayanan.text = data.harga
            }
        }
    }
}