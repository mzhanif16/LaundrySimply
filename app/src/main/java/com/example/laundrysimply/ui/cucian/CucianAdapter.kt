package com.example.laundrysimply.ui.cucian

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.laundrysimply.R
import androidx.recyclerview.widget.RecyclerView
import com.example.laundrysimply.model.response.transaksi.Data

class CucianAdapter(
    private val listData: List<Data>,
    private val itemAdapterCallback: ItemAdapterCallback,
) : RecyclerView.Adapter<CucianAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CucianAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_row_cucian, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CucianAdapter.ViewHolder, position: Int) {
       holder.bind(listData[position],itemAdapterCallback)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvIdTransaksi: TextView = itemView.findViewById(R.id.id_transaksi)
        private val tvStatusTransaksi: TextView = itemView.findViewById(R.id.status_transaksi)
        fun bind(data: Data, itemAdapterCallback: ItemAdapterCallback){
            itemView.apply {
                tvIdTransaksi.text = data.id.toString()
                tvStatusTransaksi.text = data.statusTransaksi
                if (data.statusTransaksi.equals("CANCELLED")){
                    tvStatusTransaksi.setBackgroundResource(R.drawable.shape_status_cancelled)
                }else if (data.statusTransaksi.equals("DELIVERED")){
                    tvStatusTransaksi.setBackgroundResource(R.drawable.shape_status_delivered)
                }

                itemView.setOnClickListener { itemAdapterCallback.onClick(it, data) }
            }
        }

    }

    interface ItemAdapterCallback {
        fun onClick(v: View, data: Data)
    }

}