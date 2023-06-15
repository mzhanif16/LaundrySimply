package com.example.laundrysimply.ui.detailcucian

import com.example.laundrysimply.base.BasePresenter
import com.example.laundrysimply.base.BaseView
import com.example.laundrysimply.model.response.checkout.CheckOutResponse

interface PaymentContract {
    interface View: BaseView {
        fun onPaymentSuccess(checkOutResponse: CheckOutResponse, view: android.view.View)
        fun onPaymentFailed(message:String)
    }

    interface Presenter : PaymentContract, BasePresenter {
        fun CheckOut(
            layananId: String, userId: Int, totalBayar: String, kuantitas:String, waktuPemesanan:String, waktuPengantaran:String, rating:Int, keterangan:String,
            view: android.view.View)
    }
}