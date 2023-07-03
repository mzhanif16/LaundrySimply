package com.example.laundrysimply.ui.profile

import android.net.Uri
import android.view.View
import com.example.laundrysimply.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UpdateProfilePresenter(private val view: UpdateProfileContract.View): UpdateProfileContract.Presenter {

    private val mCompositeDisposable : CompositeDisposable?
    init {
        this.mCompositeDisposable = CompositeDisposable()
    }

    override fun updateProfile(name: String, email: String, notelp: String, address: String, viewParms : View) {
        view.showLoading()
        val disposable = HttpClient.getInstance().getApi()!!.UpdateProfile(name, email, notelp, address)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.dismissLoading()
                    if (it.meta?.status.equals("success",true)){
                        it.data?.let { it1 -> view.onUpdateProfileSuccess(it1, viewParms) }
                    }else{
                        it.meta?.message?.let { it1 -> view.onUpdateProfieFailed(it1) }
                    }
                },{
                    view.dismissLoading()
                    view.onUpdateProfieFailed(it.message.toString())
                }
            )
        mCompositeDisposable!!.add(disposable)
    }

    override fun submitPhotoRegister(filePath: Uri, viewParms: View) {
        view.showLoading()

        var profileImageFile = File(filePath.path)
        var profileImageRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), profileImageFile)
        val profileImageParms = MultipartBody.Part.createFormData("file", profileImageFile.name, profileImageRequestBody)

        val disposable = HttpClient.getInstance().getApi()!!.registerPhoto(profileImageParms)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.dismissLoading()
                    if (it.meta?.status.equals("success",true)){
                        it.data?.let { it1 -> view.onRegisterPhotoSuccess(viewParms) }
                    }else{
                        view.onUpdateProfieFailed(it?.meta?.message.toString())
                    }
                },{
                    view.dismissLoading()
                    view.onUpdateProfieFailed(it.message.toString())
                }
            )
        mCompositeDisposable!!.add(disposable)
    }

    override fun subscribe() {

    }

    override fun unSubscribe() {
        mCompositeDisposable!!.clear()
    }
}