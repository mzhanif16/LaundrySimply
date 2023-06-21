package com.example.laundrysimply.ui.detailtransaksi

import android.text.Editable
import com.example.laundrysimply.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class UpdateTransaksiPresenter(private val view: UpdateTransaksiContract.View) : UpdateTransaksiContract.Presenter {

    private val mCompositeDisposable: CompositeDisposable? = CompositeDisposable()

    override fun Rating(id: Int, rating: Float, keterangan: String) {
        view.showLoading()
        val disposable = HttpClient.getInstance().getApi()!!.updateRating(id, rating, keterangan)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.dismissLoading()
                    if (it.meta?.status.equals("success",true)){
                        it.data?.let { it1 -> view.onUpdateTransaksiSuccess(it1) }
                    }else{
                        it.meta?.message?.let { it1 -> view.onUpdateTransaksiFailed(it1) }
                    }
                },{
                    view.dismissLoading()
                    view.onUpdateTransaksiFailed(it.message.toString())
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