package com.example.laundrysimply.ui.layanan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundrysimply.R
import com.example.laundrysimply.model.response.layanan.Data
import com.example.laundrysimply.utils.Helpers.formatPrice
import com.mcdev.quantitizerlibrary.HorizontalQuantitizer
import com.mcdev.quantitizerlibrary.QuantitizerListener

class LayananAdapter(
    private val listData : List<Data>,
    private val itemAdapterCallback: ItemAdapterCallback,
    private val outletId: Int,
    private val totalBayar: Int = 0

) : RecyclerView.Adapter<LayananAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_row_layanan_detail, parent, false)


        var hq: HorizontalQuantitizer = view.findViewById(R.id.hq1)
        var selectedValue = hq.value
        hq.setMinusIconColor(R.color.red)
        hq.setPlusIconColor(R.color.red)
        hq.setPlusIconBackgroundColor(R.color.white)
        hq.setMinusIconBackgroundColor(R.color.white)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LayananAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        if (data.outletId == outletId) { // Cek apakah outletId sesuai
            holder.bind(data, itemAdapterCallback)
        } else {
            holder.itemView.visibility = View.GONE // Sembunyikan item jika outletId tidak sesuai
        }
    }


    override fun getItemCount(): Int {
        return listData.size
    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvnamalayanan:TextView = itemView.findViewById(R.id.tv_nama_layanan_detail)
        val tvhargalayanan:TextView = itemView.findViewById(R.id.tv_harga_layanan_detail)
        fun bind(data: Data, itemAdapterCallback: ItemAdapterCallback){
            itemView.apply {
                tvnamalayanan.text= data.namaLayanan
                tvhargalayanan.formatPrice(data.harga)

                itemView.setOnClickListener { itemAdapterCallback.onClick(it, data) }
            }
        }

    }

    interface ItemAdapterCallback{
        fun onClick(v: View, data:Data)
    }
}