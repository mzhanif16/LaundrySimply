package com.example.laundrysimply.layanan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundrysimply.R
import com.example.laundrysimply.model.dummy.LayananModel
import com.example.laundrysimply.utils.Helpers.formatPrice

class LayananAdapter(
    private val listData : List<LayananModel>,
    private val itemAdapterCallback: ItemAdapterCallback,

) : RecyclerView.Adapter<LayananAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayananAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_row_layanan_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LayananAdapter.ViewHolder, position: Int) {
        holder.bind(listData[position], itemAdapterCallback)
    }


    override fun getItemCount(): Int {
        return listData.size
    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvnamalayanan:TextView = itemView.findViewById(R.id.tv_nama_layanan)
        val tvhargalayanan:TextView = itemView.findViewById(R.id.tv_harga_layanan)
        fun bind(data: LayananModel, itemAdapterCallback: ItemAdapterCallback){
            itemView.apply {
                tvnamalayanan.text= data.nama
                tvhargalayanan.formatPrice(data.harga)

                itemView.setOnClickListener { itemAdapterCallback.onClick(it, data) }
            }
        }

    }

    interface ItemAdapterCallback{
        fun onClick(v: View, data:LayananModel)
    }
}