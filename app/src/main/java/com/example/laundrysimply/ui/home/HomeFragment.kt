package com.example.laundrysimply.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.laundrysimply.DetailCucianActivity
import com.example.laundrysimply.layanan.DetailLayananActivity
import com.example.laundrysimply.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
}