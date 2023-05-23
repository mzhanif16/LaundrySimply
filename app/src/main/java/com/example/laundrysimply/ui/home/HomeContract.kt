package com.example.laundrysimply.ui.home

import com.example.laundrysimply.base.BasePresenter
import com.example.laundrysimply.base.BaseView
import com.example.laundrysimply.model.response.home.HomeResponse
import com.example.laundrysimply.model.response.login.LoginResponse

interface HomeContract {
    interface View: BaseView {
        fun onHomeSuccess(homeResponse: HomeResponse)
        fun onHomeFailed(message:String)
    }

    interface Presenter : HomeContract, BasePresenter {
        fun getHome()
    }
}