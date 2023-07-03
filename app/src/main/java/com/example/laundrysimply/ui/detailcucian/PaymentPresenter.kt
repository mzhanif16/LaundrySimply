package com.example.laundrysimply.ui.detailcucian

import android.view.View
import com.example.laundrysimply.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject

class PaymentPresenter(private val view: PaymentContract.View) : PaymentContract.Presenter {

    private val mCompositeDisposable: CompositeDisposable?

    init {
        this.mCompositeDisposable = CompositeDisposable()
    }

    override fun CheckOut(
        layananId: List<Int>,
        userId: Int,
        totalBayar: String,
        kuantitas: String,
        waktuPemesanan: String,
        waktuPengantaran: String,
        rating: Int,
        keterangan: String,
        outletId:Int,
        viewParms: View
    ) {
        view.showLoading()
        val requestJson = JSONObject().apply {
            put("layanan_id", JSONArray(layananId))
            put("user_id", userId)
            put("total_bayar", totalBayar)
            put("status_transaksi", "ON DELIVERY")
            put("status_bayar", "BELUM LUNAS")
            put("kuantitas", kuantitas)
            put("waktu_pemesanan", waktuPemesanan)
            put("waktu_pengantaran", waktuPengantaran)
            put("rating", rating)
            put("keterangan", keterangan)
            put("outlet_id", outletId)
        }
                val requestBody = RequestBody.create(MediaType.parse("application/json"), requestJson.toString())
                        val disposable = HttpClient.getInstance().getApi()!!.checkout(requestBody)
//        val disposable = HttpClient.getInstance().getApi()!!.checkout(
//            layananId,
//            userId,
//            totalBayar,
//            "ON DELIVERY",
//            "BELUM LUNAS",
//            kuantitas,
//            waktuPemesanan,
//            waktuPengantaran,
//            rating,
//            keterangan,
//            outletId
//        )
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