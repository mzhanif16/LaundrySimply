package com.example.laundrysimply.ui.detailcucian

import com.example.laundrysimply.base.BasePresenter
import com.example.laundrysimply.base.BaseView
import com.example.laundrysimply.model.response.checkout.CheckOutResponse1

interface PaymentContract {
    interface View: BaseView {
        fun onPaymentSuccess(checkOutResponse1: CheckOutResponse1, view: android.view.View)
        fun onPaymentFailed(message:String)
    }

    interface Presenter : PaymentContract, BasePresenter {
        fun CheckOut(
            layananId: List<Int>, userId: Int, totalBayar: String, kuantitas:String, waktuPemesanan:String, waktuPengantaran:String, rating:Int, keterangan:String, outletId:Int,
            view: android.view.View)
    }
}