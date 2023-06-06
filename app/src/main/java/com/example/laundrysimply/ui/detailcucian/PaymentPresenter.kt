package com.example.laundrysimply.ui.detailcucian

import android.view.View
import com.example.laundrysimply.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PaymentPresenter(private val view: PaymentContract.View) : PaymentContract.Presenter {

    private val mCompositeDisposable: CompositeDisposable?

    init {
        this.mCompositeDisposable = CompositeDisposable()
    }

    override fun CheckOut(
        layananId: String,
        userId: Int,
        totalBayar: String,
        kuantitas: String,
        waktuPemesanan: String,
        waktuPengantaran: String,
        rating: Int,
        keterangan: String,
        viewParms: View
    ) {
        view.showLoading()
        val disposable = HttpClient.getInstance().getApi()!!.checkout(
            layananId,
            userId,
            totalBayar,
            "ON_DELIVERY",
            "BELUM LUNAS",
            kuantitas,
            waktuPemesanan,
            waktuPengantaran,
            rating,
            keterangan
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.dismissLoading()
                    if (it.meta?.status.equals("success", true)) {
                        it.data?.let { it1 -> view.onPaymentSuccess(it1, viewParms) }
                    } else {
                        it.meta?.message?.let { it1 -> view.onPaymentFailed(it1) }
                    }
                }, {
                    view.dismissLoading()
                    view.onPaymentFailed(it.localizedMessage.toString())
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