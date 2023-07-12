package com.example.laundrysimply.ui.signin

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.laundrysimply.LaundrySimply
import com.example.laundrysimply.MainActivity
import com.example.laundrysimply.R
import com.example.laundrysimply.databinding.ActivitySignInBinding
import com.example.laundrysimply.model.response.login.LoginResponse
import com.example.laundrysimply.ui.signup.SignUpActivity
import com.google.gson.Gson
import kotlin.math.log

class SignInActivity : AppCompatActivity(), SignContract.View {

    lateinit var presenter: SigninPresenter
    private lateinit var binding: ActivitySignInBinding
    var progressDialog : Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        presenter = SigninPresenter(this)

        initView()

        binding.btnLogin.setOnClickListener {
            var email = binding.etEmail.text.toString()
            var password = binding.etKatasandi.text.toString()

            if (email.isNullOrEmpty()){
                binding.etEmail.error = "Silahkan Masukkan Email Anda"
                binding.etEmail.requestFocus()
            }else if (password.isNullOrEmpty()){
                binding.etKatasandi.error = "Silahkan Masukkan Password Anda"
                binding.etKatasandi.requestFocus()
            }else{
                presenter.submitLogin(email,password)
            }

        }
    }

    override fun onLoginSuccess(loginResponse: LoginResponse) {

        val gson = Gson()
        val json = gson.toJson(loginResponse.user)
        LaundrySimply.getApp().setToken(loginResponse.access_token)
        LaundrySimply.getApp().setUser(json)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onLoginFailed(message: String) {
        Toast.makeText(this,"Email atau Password salah, silahkan coba lagi",Toast.LENGTH_SHORT).show()
    }

    private fun initView(){
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

    fun back(view: View) {
        super.onBackPressed()
    }
}