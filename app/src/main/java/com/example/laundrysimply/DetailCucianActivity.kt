package com.example.laundrysimply

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.Layout

class DetailCucianActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_cucian)

        intent.extras.let {
            val bundle = Bundle()
            bundle.putParcelable("data", it?.get("data") as Parcelable?)
        }
    }
}