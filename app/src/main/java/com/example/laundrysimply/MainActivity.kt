package com.example.laundrysimply

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.laundrysimply.databinding.ActivityMainBinding
import com.example.laundrysimply.network.HttpClient

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = LaundrySimply.getApp().getToken()
        HttpClient.getInstance().buildRetrofitClient(token)
        Log.d("token", token.toString())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }
    override fun onBackPressed() {
        val currentDestination = findNavController(R.id.nav_host_fragment_activity_main).currentDestination

        if (currentDestination?.id == R.id.navigation_home || currentDestination?.id == R.id.navigation_cucian || currentDestination?.id == R.id.navigation_profile) {
            showExitConfirmationDialog()
        } else {
            super.onBackPressed()
        }
    }
    private fun showExitConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Apakah Anda ingin keluar dari aplikasi?")
            .setIcon(R.drawable.logo)
            .setTitle("Laundry Simply")
            .setCancelable(false)
            .setPositiveButton("Ya") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.show()
    }
}