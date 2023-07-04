package com.example.laundrysimply.ui.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.laundrysimply.LaundrySimply
import com.example.laundrysimply.R
import com.example.laundrysimply.ui.layanan.DetailLayananActivity
import com.example.laundrysimply.databinding.FragmentHomeBinding
import com.example.laundrysimply.model.response.home.HomeResponse
import com.example.laundrysimply.model.response.login.User
import com.google.gson.Gson

class HomeFragment : Fragment() , HomeContract.View{

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var presenter: HomePresenter
    var progressDialog : Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        presenter = HomePresenter(this)
        presenter.getHome()
    }

    private fun initView() {
        progressDialog = Dialog(requireContext())
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_loader, null)

        progressDialog?.let {
            it.setContentView(dialogLayout)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        var user = LaundrySimply.getApp().getUser()
        var userResponse = Gson().fromJson(user,User::class.java)
        binding.tvNamauser.setText(userResponse.name)
            Glide.with(requireActivity())
                .load(R.drawable.logo)
                .into(binding.imageView5)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onHomeSuccess(homeResponse: HomeResponse) {
        binding.tvOutletGdc.setText(homeResponse.data[0].nama)
        binding.tvAlamatOutlet1.setText(homeResponse.data[0].address)
        binding.tvOuletSukmajaya.setText(homeResponse.data[1].nama)
        binding.tvAlamatOutlet2.setText(homeResponse.data[1].address)
        binding.tvOutletCilodong.setText(homeResponse.data[2].nama)
        binding.tvAlamatOutlet3.setText(homeResponse.data[2].address)

        binding.cv1.setOnClickListener {
            val outletId = homeResponse.data[0].id // Mengambil outlet_id dari homeResponse
            val outletNama = homeResponse.data[0].nama
            val outletAlamat = homeResponse.data[0].address
            val intent = Intent(requireActivity(), DetailLayananActivity::class.java)
            intent.putExtra("outlet_id", outletId) // Menyimpan outlet_id dalam intent
            intent.putExtra("outlet_nama", outletNama)
            intent.putExtra("outlet_alamat", outletAlamat)
            startActivity(intent)
        }

        binding.cv2.setOnClickListener {
            val outletId = homeResponse.data[1].id// Mengambil outlet_id dari homeResponse
            val outletNama = homeResponse.data[1].nama
            val outletAlamat = homeResponse.data[1].address
            val intent = Intent(requireActivity(), DetailLayananActivity::class.java)
            intent.putExtra("outlet_id", outletId) // Menyimpan outlet_id dalam intent
            intent.putExtra("outlet_nama", outletNama)
            intent.putExtra("outlet_alamat", outletAlamat)
            startActivity(intent)
        }

        binding.cv3.setOnClickListener {
            val outletId = homeResponse.data[2].id// Mengambil outlet_id dari homeResponse
            val outletNama = homeResponse.data[2].nama
            val outletAlamat = homeResponse.data[2].address
            val intent = Intent(requireActivity(), DetailLayananActivity::class.java)
            intent.putExtra("outlet_id", outletId) // Menyimpan outlet_id dalam intent
            intent.putExtra("outlet_nama", outletNama)
            intent.putExtra("outlet_alamat", outletAlamat)
            startActivity(intent)
        }
    }

    override fun onHomeFailed(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.dismiss()
    }
}