package com.example.laundrysimply.ui.layanan

import com.example.laundrysimply.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LayananPresenter(private val view: LayananContract.View): LayananContract.Presenter {

    private val mCompositeDisposable : CompositeDisposable?
    init {
        this.mCompositeDisposable = CompositeDisposable()
    }

    override fun getLayanan(outletId: Int) {
        view.showLoading()
        val disposable = HttpClient.getInstance().getApi()!!.layanan()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.dismissLoading()
                    if (it.meta?.status.equals("success",true)){
                        it.data?.let { it1 -> view.onLayananSuccess(it1) }
                    }else{
                        it.meta?.message?.let { it1 -> view.onLayananFailed(it1) }
                    }
                },{
                    view.dismissLoading()
                    view.onLayananFailed(it.message.toString())
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