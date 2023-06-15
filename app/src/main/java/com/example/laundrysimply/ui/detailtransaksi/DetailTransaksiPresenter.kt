package com.example.laundrysimply.ui.detailtransaksi

import com.example.laundrysimply.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailTransaksiPresenter(private val view: DetailTransaksiContract.View) : DetailTransaksiContract.Presenter {

    private val mCompositeDisposable: CompositeDisposable? = CompositeDisposable()

    override fun getDetailTransaksi(id: Int) {
        view.showLoading()
        val disposable = HttpClient.getInstance().getApi()!!.detailtransaksi(id)
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