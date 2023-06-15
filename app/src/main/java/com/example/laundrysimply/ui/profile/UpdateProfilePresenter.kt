package com.example.laundrysimply.ui.profile

import com.example.laundrysimply.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class UpdateProfilePresenter(private val view: UpdateProfileContract.View): UpdateProfileContract.Presenter {

    private val mCompositeDisposable : CompositeDisposable?
    init {
        this.mCompositeDisposable = CompositeDisposable()
    }

    override fun updateProfile(name: String, email: String, notelp: String, address: String) {
        view.showLoading()
        val disposable = HttpClient.getInstance().getApi()!!.UpdateProfile(name, email, notelp, address)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.dismissLoading()
                    if (it.meta?.status.equals("success",true)){
                        it.data?.let { it1 -> view.onUpdateProfileSuccess(it1) }
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

    override fun subscribe() {

    }

    override fun unSubscribe() {
        mCompositeDisposable!!.clear()
    }
}