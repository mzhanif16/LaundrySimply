package com.example.laundrysimply.ui.signup

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.view.View
import com.example.laundrysimply.model.request.RegisterRequest
import com.example.laundrysimply.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

class SignUpPresenter(private val view: SignUpContract.View): SignUpContract.Presenter {

    private val mCompositeDisposable : CompositeDisposable?
    init {
        this.mCompositeDisposable = CompositeDisposable()
    }

    override fun submitRegister(registerRequest: RegisterRequest, viewParms:View) {
        view.showLoading()
        val disposable = HttpClient.getInstance().getApi()!!.register(
            registerRequest.name,
            registerRequest.email,
            registerRequest.password,
            registerRequest.password_confirmation,
            registerRequest.address,
            registerRequest.notelp
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.dismissLoading()
                    if (it.meta?.status.equals("success",true)){
                        it.data?.let { it1 -> view.onRegisterSuccess(it1, viewParms) }
                    }else{
                        view.onRegisterFailed(it.meta?.message.toString())
                    }
                },{
                    view.dismissLoading()
                    view.onRegisterFailed(it.message.toString())
                }
            )
        mCompositeDisposable!!.add(disposable)
    }


    override fun submitPhotoRegister(filePath: Uri, viewParms:View) {
        view.showLoading()

        val contentResolver: ContentResolver = viewParms.context.contentResolver
        val inputStream = contentResolver.openInputStream(filePath)

        val fileName = getFileName(filePath, contentResolver)
        val file = createFileInCacheDir(viewParms.context, fileName)
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        var profileImageFile = File(filePath.path!!)
        var profileImageRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileName)
        val profileImageParms = MultipartBody.Part.createFormData("file", fileName, profileImageRequestBody)

        val disposable = HttpClient.getInstance().getApi()!!.registerPhoto(profileImageParms)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.dismissLoading()
                    if (it.meta?.status.equals("success",true)){
                        it.data?.let { it1 -> view.onRegisterPhotoSuccess(viewParms) }
                    }else{
                       view.onRegisterFailed(it?.meta?.message.toString())
                    }
                },{
                    view.dismissLoading()
                    view.onRegisterFailed(it.message.toString())
                }
            )
        mCompositeDisposable!!.add(disposable)
    }

    override fun subscribe() {

    }

    override fun unSubscribe() {
        mCompositeDisposable!!.clear()
    }
    // Fungsi untuk mendapatkan nama file dari Uri
    private fun getFileName(uri: Uri, contentResolver: ContentResolver): String {
        val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(columnIndex)
            }
        }
        return ""
    }
    // Fungsi untuk membuat file di direktori cache aplikasi
    private fun createFileInCacheDir(context: Context, fileName: String): File {
        val cacheDir = context.cacheDir
        return File(cacheDir, fileName)
    }
}