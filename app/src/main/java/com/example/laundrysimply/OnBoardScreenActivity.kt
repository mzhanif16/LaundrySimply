package com.example.laundrysimply

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.laundrysimply.databinding.ActivityOnBoardScreenBinding
import com.example.laundrysimply.databinding.ActivitySignInBinding
import com.example.laundrysimply.ui.signin.SignInActivity
import com.example.laundrysimply.ui.signup.SignUpActivity

class OnBoardScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if(!LaundrySimply.getApp().getToken().isNullOrEmpty()){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btndaftar.setOnClickListener {
         val signup = Intent(this,SignUpActivity::class.java)
         startActivity(signup)
        }
        binding.btnmasuk.setOnClickListener {
            val signin = Intent(this,SignInActivity::class.java)
            startActivity(signin)
        }
    }
}