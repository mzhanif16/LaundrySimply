package com.example.laundrysimply.ui.signup

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
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.laundrysimply.LaundrySimply
import com.example.laundrysimply.R
import com.example.laundrysimply.databinding.ActivitySignUpBinding
import com.example.laundrysimply.model.request.RegisterRequest
import com.example.laundrysimply.model.response.login.LoginResponse
import com.example.laundrysimply.ui.signin.SignInActivity
import com.google.gson.Gson

class SignUpActivity : AppCompatActivity(), SignUpContract.View {
    var filePath: Uri? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 1
    lateinit var presenter: SignUpPresenter
    var progressDialog: Dialog? = null
    private lateinit var data: RegisterRequest
    private lateinit var binding: ActivitySignUpBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        presenter = SignUpPresenter(this)

        initListener()
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initListener() {
        binding.ivProfil.setOnClickListener {
            val options = arrayOf<CharSequence>("Ambil Foto", "Pilih dari Galeri", "Batal")

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pilih Foto")
            builder.setItems(options) { dialog, item ->
                when {
                    options[item] == "Ambil Foto" -> {
                        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        if (takePicture.resolveActivity(packageManager) != null) {
                            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE)
                        } else {
                            Toast.makeText(this, "Tidak ada aplikasi kamera", Toast.LENGTH_SHORT).show()
                        }
                    }
                    options[item] == "Pilih dari Galeri" -> {
                        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK)
                    }
                    options[item] == "Batal" -> {
                        dialog.dismiss()
                    }
                }
            }
            builder.show()
        }

        binding.btnDaftar.setOnClickListener {
            var fullname = binding.tvNama.text.toString()
            var email = binding.tvEmail.text.toString()
            var password = binding.tvKatasandi.text.toString()
            var confirmpassword = binding.tvKonfirmasisandi.text.toString()
            var notelp = binding.tvNotelp.text.toString()
            var alamat = binding.tvAlamatuser.text.toString()
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
                alamat,
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
            } else if (alamat.isNullOrEmpty()) {
                binding.tvNotelp.error = "Silahkan masukkan alamat !"
                binding.tvNotelp.requestFocus()
            } else {
                presenter.submitRegister(data, it)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            filePath = data?.data
            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivProfil)
        }else if(requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK ){
            filePath = data?.data
            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivProfil)
        }else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        }
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