package com.example.laundrysimply.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.laundrysimply.LaundrySimply
import com.example.laundrysimply.R
import com.example.laundrysimply.databinding.FragmentProfileBinding
import com.example.laundrysimply.signin.SignInActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivLogout.setOnClickListener {
            logout()
            val intent = Intent(activity, SignInActivity::class.java)
//            startActivity(intent)
        }
    }
    private fun logout() {
        val laundrySimply = LaundrySimply()
        laundrySimply.removeToken()
    }
}