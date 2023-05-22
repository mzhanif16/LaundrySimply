package com.example.laundrysimply.signup

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.laundrysimply.LaundrySimply
import com.example.laundrysimply.R
import com.example.laundrysimply.databinding.ActivitySignUpBinding
import com.example.laundrysimply.model.request.RegisterRequest
import com.example.laundrysimply.model.response.login.LoginResponse
import com.example.laundrysimply.signin.SignInActivity
import com.google.gson.Gson

class SignUpActivity : AppCompatActivity(), SignUpContract.View {
    var filePath: Uri? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    lateinit var presenter: SignUpPresenter
    var progressDialog: Dialog? = null
    private lateinit var data: RegisterRequest
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        presenter = SignUpPresenter(this)

        initDummy()
        initListener()
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK){
//            filePath = data?.data
//
//            Glide.with(this)
//                .load(filePath)
//                .apply(RequestOptions.circleCropTransform())
//                .into(binding.ivProfil)
//        } else if (resultCode == ImagePicker.RESULT_ERROR){
//            Toast.makeText(this,ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//        } else{
//            Toast.makeText(this,"Task Cancelled ", Toast.LENGTH_SHORT).show()
//        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            filePath = data?.data
            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivProfil)
        } else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initListener() {
//        binding.ivProfil.setOnClickListener{
//            ImagePicker.with(this)
//                .cameraOnly()
//                .start()
//        }
        binding.ivProfil.setOnClickListener {
            val takepicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takepicture.resolveActivity(packageManager) != null) {
                startActivityForResult(takepicture, REQUEST_IMAGE_CAPTURE)
            } else {
                Toast.makeText(this, "Tidak ada aplikasi kamera", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDaftar.setOnClickListener {
            var fullname = binding.tvNama.text.toString()
            var email = binding.tvEmail.text.toString()
            var password = binding.tvKatasandi.text.toString()
            var confirmpassword = binding.tvKonfirmasisandi.text.toString()
            var notelp = binding.tvNotelp.text.toString()
            val bundle = intent.extras
            if (bundle != null) {
                data = bundle.getParcelable("data")!!
            }
            data = RegisterRequest(
                fullname,
                email,
                notelp,
                password,
                confirmpassword,
                "",
                filePath
            )

            if (fullname.isNullOrEmpty()) {
                binding.tvNama.error = "Silahkan masukkan nama !"
                binding.tvNama.requestFocus()
            } else if (email.isNullOrEmpty()) {
                binding.tvEmail.error = "Silahkan masukkan email !"
                binding.tvEmail.requestFocus()
            } else if (password.isNullOrEmpty()) {
                binding.tvKatasandi.error = "Silahkan masukkan password !"
                binding.tvKatasandi.requestFocus()
            } else if (!confirmpassword.equals(password)) {
                binding.tvKonfirmasisandi.error = "Konfirmasi password harus sama dengan password"
                binding.tvKonfirmasisandi.requestFocus()
            } else if (notelp.isNullOrEmpty()) {
                binding.tvNotelp.error = "Silahkan masukkan no telp !"
                binding.tvNotelp.requestFocus()
            } else {
                presenter.submitRegister(data, it)
            }
        }
    }

    private fun initDummy() {
        binding.tvNama.setText("Mz Hanif")
        binding.tvEmail.setText("mz@gmail.com")
        binding.tvKatasandi.setText("hanif53gan")
        binding.tvNotelp.setText("081122334439")
        binding.tvKonfirmasisandi.setText("hanif53gan")
    }

    override fun onRegisterSuccess(loginResponse: LoginResponse, view: View) {
        LaundrySimply.getApp().setToken(loginResponse.access_token)

        val gson = Gson()
        val json = gson.toJson(loginResponse.user)
        LaundrySimply.getApp().setUser(json)

        if (data.filePath == null) {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        } else {
            presenter.submitPhotoRegister(data.filePath!!, view)
        }
    }

    override fun onRegisterPhotoSuccess(view: View) {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    override fun onRegisterFailed(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun initView() {
        progressDialog = Dialog(this)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_loader, null)

        progressDialog?.let {
            it.setContentView(dialogLayout)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.dismiss()
    }
}