package com.example.laundrysimply.ui.detailtransaksi

import android.text.Editable
import com.example.laundrysimply.base.BasePresenter
import com.example.laundrysimply.base.BaseView
import com.example.laundrysimply.model.response.transaksi.UpdateTransaksiResponse

interface UpdateTransaksiContract {
    interface View: BaseView {
        fun onUpdateTransaksiSuccess(updateTransaksiPresenter: UpdateTransaksiResponse)
        fun onUpdateTransaksiFailed(message:String)
    }

    interface Presenter : UpdateTransaksiContract, BasePresenter {
        fun Rating(id:Int, rating: Float, keterangan: String)
    }
}