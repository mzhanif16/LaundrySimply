package com.example.laundrysimply.utils

import android.widget.TextView
import java.text.DecimalFormat

object Helpers {

    fun TextView.formatPrice(value: String){
        this.text = getCurrencyIDR(java.lang.Double.parseDouble(value))
    }

    fun getCurrencyIDR (harga : Double) : String {
        val format = DecimalFormat("#,##,###")
        return "Rp " +format.format(harga).replace(",".toRegex(),".")
    }
}