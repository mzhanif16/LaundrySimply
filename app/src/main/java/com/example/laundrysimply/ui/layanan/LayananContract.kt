package com.example.laundrysimply.ui.layanan

import com.example.laundrysimply.base.BasePresenter
import com.example.laundrysimply.base.BaseView
import com.example.laundrysimply.model.response.layanan.LayananResponse

interface LayananContract {
    interface View: BaseView {
        fun onLayananSuccess(layananResponse: LayananResponse)
        fun onLayananFailed(message:String)
    }

    interface Presenter : LayananContract, BasePresenter {
        fun getLayanan(outledId: Int)
    }
}