package com.example.laundrysimply.ui.detailtransaksi

import com.example.laundrysimply.base.BasePresenter
import com.example.laundrysimply.base.BaseView
import com.example.laundrysimply.model.response.detailtransaksi.DetailTransaksiResponse
import com.example.laundrysimply.model.response.transaksi.TransaksiResponse

interface DetailTransaksiContract {
    interface View: BaseView {
        fun onTransaksiSuccess(detailTransaksiResponse: DetailTransaksiResponse)
        fun onTransaksiFailed(message:String)
    }

    interface Presenter : DetailTransaksiContract, BasePresenter {
        fun getDetailTransaksi(id:Int)
    }
}