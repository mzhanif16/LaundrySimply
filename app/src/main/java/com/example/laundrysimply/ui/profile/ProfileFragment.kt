package com.example.laundrysimply.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.laundrysimply.LaundrySimply
import com.example.laundrysimply.R
import com.example.laundrysimply.databinding.FragmentProfileBinding
import com.example.laundrysimply.model.response.login.User
import com.example.laundrysimply.model.response.profile.UpdateProfileResponse
import com.example.laundrysimply.network.Endpoint
import com.example.laundrysimply.network.HttpClient
import com.example.laundrysimply.ui.signin.SignInActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfileFragment : Fragment(), UpdateProfileContract.View {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var endpoint: Endpoint
    private lateinit var laundrysimply: LaundrySimply
    var progressDialog : Dialog? = null
    lateinit var presenter: UpdateProfilePresenter
    private val REQUEST_IMAGE_PICK = 1
    private val REQUEST_IMAGE_CAPTURE = 1
    var filePath: Uri?= null

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
        presenter = UpdateProfilePresenter(this)
        initListener()
        initView()

        binding.ibLogout.setOnClickListener {
            val accessToken = laundrysimply.getToken()
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setIcon(R.drawable.logo)
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

        var user = laundrysimply.getUser()
        var userResponse = Gson().fromJson(user, User::class.java)

        binding.tvNama.setText(userResponse.name)
        binding.tvEmail.setText(userResponse.email)
        binding.tvNotelp.setText(userResponse.notelp.toString())
        binding.tvAlamat.setText(userResponse.address.toString())

        binding.tvNama.isEnabled = false
        binding.tvEmail.isEnabled = false
        binding.tvAlamat.isEnabled = false
        binding.tvNotelp.isEnabled = false
        binding.imageView9.isClickable = false


        var isEditMode = false

        binding.btnSimpan.setOnClickListener {
            if(!isEditMode){
                isEditMode = true
                binding.tvNama.isEnabled = true
                binding.tvEmail.isEnabled = true
                binding.tvAlamat.isEnabled = true
                binding.tvNotelp.isEnabled = true
                binding.imageView9.isClickable = true
                binding.btnSimpan.text = "Simpan"
            }else{
                isEditMode = false
                binding.tvNama.isEnabled = false
                binding.tvEmail.isEnabled = false
                binding.tvAlamat.isEnabled = false
                binding.tvNotelp.isEnabled = false
                binding.imageView9.isClickable = false
                binding.btnSimpan.text = "Edit Profile"

                val nama = binding.tvNama.text.toString()
                val email = binding.tvEmail.text.toString()
                val alamat = binding.tvAlamat.text.toString()
                val notelp = binding.tvNotelp.text.toString()

                presenter.updateProfile(nama,email,notelp,alamat,it)
            }
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

    private fun initView(){
        progressDialog = Dialog(requireContext())
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_loader, null)

        progressDialog?.let {
            it.setContentView(dialogLayout)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
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

    private fun initListener() {
        binding.imageView9.setOnClickListener {
            val options = arrayOf<CharSequence>("Ambil Foto", "Pilih dari Galeri", "Batal")

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Pilih Foto")
            builder.setItems(options) { dialog, item ->
                when {
                    options[item] == "Ambil Foto" -> {
                        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE)
                    }
                    options[item] == "Pilih dari Galeri" -> {
                        val pickPhoto =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK)
                    }
                    options[item] == "Batal" -> {
                        dialog.dismiss()
                    }
                }
            }
            builder.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            filePath = data?.data
            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.imageView9)
        }else if(requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK){
            filePath = data?.data
            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.imageView9)
        }

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

    override fun onUpdateProfileSuccess(updateProfileResponse: UpdateProfileResponse, view: View) {
        Toast.makeText(context, "Sukses Update Profile", Toast.LENGTH_SHORT).show()

        filePath?.let { presenter.submitPhotoRegister(it, view) }
    }

    override fun onRegisterPhotoSuccess(view: View) {
        TODO("Not yet implemented")
    }

    override fun onUpdateProfieFailed(message: String) {
        Toast.makeText(context, message,Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.dismiss()
    }
}
