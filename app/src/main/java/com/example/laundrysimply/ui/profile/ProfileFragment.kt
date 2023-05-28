package com.example.laundrysimply.ui.profile

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.laundrysimply.LaundrySimply
import com.example.laundrysimply.R
import com.example.laundrysimply.databinding.FragmentProfileBinding
import com.example.laundrysimply.network.Endpoint
import com.example.laundrysimply.network.HttpClient
import com.example.laundrysimply.ui.signin.SignInActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var endpoint: Endpoint
    private lateinit var laundrysimply: LaundrySimply

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        endpoint = HttpClient.getInstance().getApi() ?: throw IllegalStateException("ApiService is null")
        laundrysimply = LaundrySimply.getApp()

        binding.ibLogout.setOnClickListener {
            val accessToken = laundrysimply.getToken()
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setIcon(R.drawable.ic_launcher_foreground)
            alertDialogBuilder.setTitle("Konfirmasi Logout")
            alertDialogBuilder.setMessage("Apakah Anda yakin ingin logout?")
            alertDialogBuilder.setPositiveButton("Ya") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                if (accessToken != null) {
                logout(accessToken)
            }
            }
            alertDialogBuilder.setNegativeButton("Tidak") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    @SuppressLint("CheckResult")
    private fun logout(accessToken: String) {
        val authorizationHeader = "Bearer $accessToken"
        endpoint.logout(authorizationHeader)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    if (response.meta.code == 200) {
                        // Logout berhasil
                        showLogoutSuccessDialog()
                        laundrysimply.removeToken() // Hapus token dari SharedPreferences

                    } else {
                        showLogoutFailedDialog()
                    }
                },
                { error ->
                    Toast.makeText(requireContext(), "Logout Gagal.", Toast.LENGTH_SHORT).show()
                }
            )
    }

    private fun showLogoutSuccessDialog() {
        if (!requireActivity().isFinishing) {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("Logout Successful")
            alertDialogBuilder.setMessage("You have been successfully logged out.")
            alertDialogBuilder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                navigateToSignInActivity()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun showLogoutFailedDialog() {
        if (!requireActivity().isFinishing) {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("Logout Failed")
            alertDialogBuilder.setMessage("Failed to logout. Please try again.")
            alertDialogBuilder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun showLogoutConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Konfirmasi Logout")
        alertDialogBuilder.setMessage("Apakah Anda yakin ingin logout?")
        alertDialogBuilder.setPositiveButton("Ya") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
            // Logout dilakukan setelah pengguna memilih "Ya"

        }
        alertDialogBuilder.setNegativeButton("Tidak") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
            // Tidak melakukan logout jika pengguna memilih "Tidak"
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun navigateToSignInActivity() {
        val intent = Intent(activity, SignInActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
