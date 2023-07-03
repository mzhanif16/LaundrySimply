package com.example.laundrysimply.ui.profile

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.SipAddress
import android.view.View
import com.example.laundrysimply.base.BasePresenter
import com.example.laundrysimply.base.BaseView
import com.example.laundrysimply.model.response.login.LoginResponse
import com.example.laundrysimply.model.response.profile.UpdateProfileResponse

interface UpdateProfileContract {
    interface View: BaseView {
        fun onUpdateProfileSuccess(updateProfileResponse: UpdateProfileResponse, view: android.view.View)
        fun onRegisterPhotoSuccess   (view:android.view.View)
        fun onUpdateProfieFailed(message:String)
    }

    interface Presenter : UpdateProfileContract, BasePresenter {
        fun updateProfile(name: String, email: String, notelp: String, address: String, view: android.view.View)
        fun submitPhotoRegister(filePath: Uri, view:android.view.View)
    }
}