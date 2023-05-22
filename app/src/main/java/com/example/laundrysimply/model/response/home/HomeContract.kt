package com.example.laundrysimply.model.response.home

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.laundrysimply.base.BasePresenter
import com.example.laundrysimply.base.BaseView
import com.example.laundrysimply.model.response.login.LoginResponse

interface HomeContract {
    interface View: BaseView {
        fun onLoginSuccess(loginResponse: LoginResponse)
        fun onLoginFailed(message:String)
    }

    interface Presenter : HomeContract, BasePresenter {
        fun submitLogin(email: String, password: String)
    }
}