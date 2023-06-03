package com.example.laundrysimply

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.laundrysimply.databinding.ActivityPilihWaktuBinding
import com.example.laundrysimply.model.response.layanan.Data
import com.example.laundrysimply.model.response.login.User
import com.example.laundrysimply.ui.detailcucian.DetailCucianActivity
import com.google.gson.Gson
import java.util.Calendar

class PilihWaktuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPilihWaktuBinding
    var tahun: Int = 0
    var bulan: Int = 0
    var tanggal: Int = 0

    var tahun2: Int = 0
    var bulan2: Int = 0
    var tanggal2: Int = 0

    private var totalKuantitas: Int = 0
    private var totalBayar: Int = 0
    private var outletNama: String = ""
    private var outletAlamat: String = ""
    private lateinit var dataA: ArrayList<Data>
    private var tanggalPickup: String = ""
    private var tanggalDelivery: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihWaktuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var user = LaundrySimply.getApp().getUser()
        var userResponse = Gson().fromJson(user, User::class.java)
        binding.tvAlamatUser.text = userResponse.address.toString()


        val bundle = intent.extras
        if (bundle != null){
            val totalKuantitas = bundle.getInt("total_kuantitas", 0)
            val totalBayar = bundle.getInt("total_bayar", 0)
            val outletNama = bundle.getString("outlet_nama")
            val outletAlamat = bundle.getString("outlet_alamat")
            val dataA = bundle.getParcelableArrayList<Data>("data") as ArrayList<Data>?

//            binding.kuantitas.text = "Total Kuantitas : $totalKuantitas"
//            binding.bayar.text = "Total Bayar : $totalBayar"
//            binding.namaLayanan.text = "$outletNama : $outletAlamat"

            val intent = Intent(this, DetailCucianActivity::class.java)
            binding.btnLanjut.setOnClickListener {
                intent.putExtra("total_kuantitas", totalKuantitas)
                intent.putExtra("total_bayar", totalBayar)
                intent.putExtra("outlet_nama", outletNama)
                intent.putExtra("outlet_alamat", outletAlamat)
                intent.putExtra("data", dataA)
                intent.putExtra("tanggal_pickup", tanggalPickup)
                intent.putExtra("tanggal_delivery", tanggalDelivery)
                startActivity(intent)


            }
        }


        binding.btnTanggalPickup.setOnClickListener {
            val calendar = Calendar.getInstance()
            tahun = calendar.get(Calendar.YEAR)
            bulan = calendar.get(Calendar.MONTH)
            tanggal = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog: DatePickerDialog
            dialog = DatePickerDialog(this@PilihWaktuActivity, { view, year, month, dayOfMonth ->
                tahun = year
                bulan = month
                tanggal = dayOfMonth
                tanggalPickup = "$tanggal - $bulan - $tahun"
                binding.etTanggalPickup.setText(tanggalPickup)
            }, tahun, bulan, tanggal)
            dialog.show()

        }
        binding.btnTanggalDeliv.setOnClickListener {
            val calendar = Calendar.getInstance()
            tahun2 = calendar.get(Calendar.YEAR)
            bulan2 = calendar.get(Calendar.MONTH)
            tanggal2 = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog: DatePickerDialog
            dialog = DatePickerDialog(this@PilihWaktuActivity, { view, year, month, dayOfMonth ->
                tahun2 = year
                bulan2 = month
                tanggal2 = dayOfMonth

                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)

                val pickupCalendar = Calendar.getInstance()
                pickupCalendar.set(tahun, bulan, tanggal)

                if(selectedCalendar > pickupCalendar ){
                    tanggalDelivery = "$tanggal2 - $bulan2 - $tahun2"
                    binding.etTanggalDeliv.setText(tanggalDelivery)
                } else {
                    Toast.makeText(this@PilihWaktuActivity, "Tanggal Delivery tidak boleh kurang dari atau sama dengan tanggal pick up",Toast.LENGTH_SHORT).show()
                }
            }, tahun2, bulan2, tanggal2)
            dialog.show()
        }
    }
}