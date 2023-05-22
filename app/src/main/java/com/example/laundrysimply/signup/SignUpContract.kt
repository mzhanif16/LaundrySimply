package com.example.laundrysimply.signup

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.laundrysimply.base.BasePresenter
import com.example.laundrysimply.base.BaseView
import com.example.laundrysimply.model.request.RegisterRequest
import com.example.laundrysimply.model.response.login.LoginResponse

interface SignUpContract {
    interface View: BaseView {
        fun onRegisterSuccess   (loginResponse: LoginResponse, view:android.view.View)
        fun onRegisterPhotoSuccess   (view:android.view.View)
        fun onRegisterFailed(message:String)
    }

    interface Presenter : SignUpContract, BasePresenter {
        fun submitRegister(registerRequest: RegisterRequest, view:android.view.View)
        fun submitPhotoRegister(filePath:Uri, view:android.view.View)
    }
}