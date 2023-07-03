package com.example.laundrysimply.ui.cucian

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundrysimply.LaundrySimply
import com.example.laundrysimply.R
import com.example.laundrysimply.databinding.FragmentCucianBinding
import com.example.laundrysimply.model.response.login.LoginResponse
import com.example.laundrysimply.model.response.transaksi.Data
import com.example.laundrysimply.model.response.transaksi.TransaksiResponse
import com.example.laundrysimply.model.response.transaksi.User
import com.example.laundrysimply.network.HttpClient
import com.example.laundrysimply.ui.detailtransaksi.DetailTransaksiActivity
import com.google.gson.Gson

class CucianFragment : Fragment(), CucianAdapter.ItemAdapterCallback, TransaksiContract.View {
    private var _binding: FragmentCucianBinding? = null
    private val binding get() = _binding!!
    private var transaksiList : ArrayList<Data> = ArrayList()
    var progressDialog : Dialog? = null
    lateinit var presenter: TransaksiPresenter
    private var userId: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCucianBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        presenter = TransaksiPresenter(this)
        var user = LaundrySimply.getApp().getUser()
        var userResponse = Gson().fromJson(user, User::class.java)
        userId = userResponse.id
        presenter.getTransaksi()

    }

    private fun initView(){
        progressDialog = Dialog(requireContext())
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_loader, null)

        progressDialog?.let {
            it.setContentView(dialogLayout)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun onClick(v: View, data: Data) {
        val intent = Intent(context,DetailTransaksiActivity::class.java)
        intent.putExtra("transaksi_id", data.id)
        startActivity(intent)
    }

    override fun onTransaksiSuccess(transaksiResponse: TransaksiResponse) {
        if(transaksiResponse.data.isNullOrEmpty()){
            binding.tvInformasi.visibility = View.VISIBLE
        }else{
            transaksiList.clear()
            transaksiList.addAll(transaksiResponse.data)
            var adapter = CucianAdapter(transaksiList,this)
            var layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)
            binding.rvTransaksi.layoutManager = layoutManager
            binding.rvTransaksi.adapter = adapter
        }
    }

    override fun onTransaksiFailed(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.dismiss()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}