// LayananAdapter.kt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundrysimply.R
import com.example.laundrysimply.model.response.layanan.Data
import com.example.laundrysimply.model.response.layanan.LayananItem
import com.example.laundrysimply.utils.Helpers.formatPrice
import com.mcdev.quantitizerlibrary.HorizontalQuantitizer
import com.mcdev.quantitizerlibrary.QuantitizerListener

class LayananAdapter(
    private val listData: List<Data>,
    private val itemAdapterCallback: ItemAdapterCallback,
    private val outletId: Int,
    private val onTotalKuantitasUpdated: (Int) -> Unit,
    private val onTotalBayarUpdated: (Int) -> Unit
) : RecyclerView.Adapter<LayananAdapter.ViewHolder>() {
    private val selectedValues = HashMap<Int, Int>()
    private var totalKuantitas: Int = 0
    private var totalBayar: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_row_layanan_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LayananAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        //validasi data apakah outlet id yang berada di outlet sama dengan outlet id yang ada di layanan
        if (data.outletId == outletId) {
            holder.bind(data, itemAdapterCallback)
            holder.updateTotalBayar()
            holder.updateTotalKuantitas()
        } else {
            holder.itemView.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    //menghitung total bayar berdasarkan nilai kuantitas yang dipilih untuk setiap layanan.
    fun calculateTotalBayar() {
        var totalBayar = 0
        for ((position, value) in selectedValues) {
            val data = listData[position]
            val harga = data.harga.toInt()
            val totalHargaItem = harga * value
            totalBayar += totalHargaItem
        }
        onTotalBayarUpdated(totalBayar)
    }

    //menghitung total kuantitas berdasarkan nilai kuantitas yang dipilih untuk setiap layanan.
    fun calculateTotalKuantitas() {
        var totalKuantitas = 0
        for (value in selectedValues.values) {
            totalKuantitas += value
        }
        onTotalKuantitasUpdated(totalKuantitas)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNamaLayanan: TextView = itemView.findViewById(R.id.tv_nama_layanan_detail)
        private val tvHargaLayanan: TextView = itemView.findViewById(R.id.tv_harga_layanan_detail)
        private val hq: HorizontalQuantitizer = itemView.findViewById(R.id.hq1)

        init {
            hq.setMinusIconColor(R.color.red)
            hq.setPlusIconColor(R.color.red)
            hq.setPlusIconBackgroundColor(R.color.white)
            hq.setMinusIconBackgroundColor(R.color.white)
            hq.setQuantitizerListener(object : QuantitizerListener {
                override fun onDecrease() {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val data = listData[position]
                        val currentValue = selectedValues[position] ?: 0
                        if (currentValue > 0) {
                            selectedValues[position] = currentValue
                            updateTotalBayar()
                            updateTotalKuantitas()
                        }
                    }
                }

                override fun onIncrease() {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val data = listData[position]
                        val currentValue = selectedValues[position] ?: 0
                        selectedValues[position] = currentValue
                        updateTotalBayar()
                        updateTotalKuantitas()
                    }
                }

                override fun onValueChanged(value: Int) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val data = listData[position]
                        selectedValues[position] = value
                        updateTotalBayar()
                        updateTotalKuantitas()
                    }
                }
            })
        }

        fun bind(data: Data, itemAdapterCallback: ItemAdapterCallback) {
            itemView.apply {
                tvNamaLayanan.text = data.namaLayanan
                tvHargaLayanan.formatPrice(data.harga)

                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val value = selectedValues[position] ?: 0
                    hq.value = value
                }

                itemView.setOnClickListener { itemAdapterCallback.onClick(it, data) }
            }
        }

        fun updateTotalBayar() {
            calculateTotalBayar()
        }

        fun updateTotalKuantitas() {
            calculateTotalKuantitas()
        }
    }

    interface ItemAdapterCallback {
        fun onClick(v: View, data: Data)
    }
}
