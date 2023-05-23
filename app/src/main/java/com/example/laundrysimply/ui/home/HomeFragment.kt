package com.example.laundrysimply.ui.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.laundrysimply.DetailCucianActivity
import com.example.laundrysimply.R
import com.example.laundrysimply.layanan.DetailLayananActivity
import com.example.laundrysimply.databinding.FragmentHomeBinding
import com.example.laundrysimply.model.response.home.HomeResponse

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cv1.setOnClickListener {
            val intent = Intent(requireActivity(), DetailCucianActivity::class.java)
            startActivity(intent)
        }

        binding.cv2.setOnClickListener {
            val intent = Intent(requireActivity(), DetailLayananActivity::class.java)
            startActivity(intent)
        }
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
    }

//    private fun pindahActivity(){
//        binding.cv1.setOnClickListener {
//            val intent = Intent(activity, SignInActivity::class.java)
//            startActivity(intent)
//        }
//    }

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