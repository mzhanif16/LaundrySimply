package com.example.laundrysimply.ui.detailcucian

import com.example.laundrysimply.base.BasePresenter
import com.example.laundrysimply.base.BaseView
import com.example.laundrysimply.model.response.checkout.CheckOutResponse
import com.example.laundrysimply.model.response.home.HomeResponse
import com.example.laundrysimply.model.response.login.LoginResponse

interface PaymentContract {
    interface View: BaseView {
        fun onPaymentSuccess(checkOutResponse: CheckOutResponse)
        fun onPaymentFailed(message:String)
    }

    interface Presenter : PaymentContract, BasePresenter {
        fun getCheckOut()
    }
}