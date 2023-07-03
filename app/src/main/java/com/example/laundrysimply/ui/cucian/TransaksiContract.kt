package com.example.laundrysimply.ui.cucian

import com.example.laundrysimply.base.BasePresenter
import com.example.laundrysimply.base.BaseView
import com.example.laundrysimply.model.response.transaksi.TransaksiResponse

interface TransaksiContract {
    interface View: BaseView {
        fun onTransaksiSuccess(transaksiResponse: TransaksiResponse)
        fun onTransaksiFailed(message:String)
    }

    interface Presenter : TransaksiContract, BasePresenter {
        fun getTransaksi()
    }
}