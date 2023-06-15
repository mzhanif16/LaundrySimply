package com.example.laundrysimply.ui.cucian

import com.example.laundrysimply.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TransaksiPresenter(private val view: TransaksiContract.View) : TransaksiContract.Presenter {

    private val mCompositeDisposable: CompositeDisposable? = CompositeDisposable()

    override fun getTransaksi() {
        view.showLoading()
        val disposable = HttpClient.getInstance().getApi()!!.transaksi()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.dismissLoading()
                    if (it.meta?.status.equals("success",true)){
                        it.data?.let { it1 -> view.onTransaksiSuccess(it1) }
                    }else{
                        it.meta?.message?.let { it1 -> view.onTransaksiFailed(it1) }
                    }
                },{
                    view.dismissLoading()
                    view.onTransaksiFailed(it.message.toString())
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