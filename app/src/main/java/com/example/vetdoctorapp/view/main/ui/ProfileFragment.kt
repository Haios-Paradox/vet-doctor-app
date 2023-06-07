package com.example.vetdoctorapp.view.main.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vetdoctorapp.controller.main.MainViewModel
import com.example.vetdoctorapp.databinding.FragmentProfileBinding
import com.example.vetdoctorapp.model.data.User

class ProfileFragment : Fragment() {

    //TODO: Make user go here first, complete profile validation.

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : FragmentProfileBinding
    private val cameraPermission = android.Manifest.permission.CAMERA
    private val storagePermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val mediaPermission = android.Manifest.permission.ACCESS_MEDIA_LOCATION

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        binding.ivProfileImage.setOnClickListener {
            dispatchTakePictureIntent()
        }

        mainViewModel.imageBitmap.observe(requireActivity()){
            if(it!=null)
                Glide.with(binding.ivProfileImage).load(it).into(binding.ivProfileImage)
        }

        mainViewModel.userData.observe(requireActivity()){
            if (it!=null)
                setupView(it)
        }
        binding.fabEditProfile.setOnClickListener {
            saveData()
        }

        return binding.root
    }

    private fun saveData() {
        val user = User(
            avatar = mainViewModel.userData.value?.avatar,
            name = binding.edProfileName.text.toString(),
            specialist = binding.edProfileSpec.selectedItem.toString(),
            email = binding.edProfileEmail.text.toString(),
            phone = binding.edHomePhone.text.toString(),
            experience = binding.edProfileExperience.text.toString(),
            description = binding.edProfileDesc.text.toString()
        )
        if(mainViewModel.imageBitmap.value!=null)
            mainViewModel.updateProfile(user, mainViewModel.imageBitmap.value!!)
        else
            mainViewModel.updateProfile(user)
    }

    private fun setupView(user: User) {
        with(binding){
           Glide.with(binding.ivProfileImage).load(user.avatar).into(binding.ivProfileImage)
            edProfileName.setText(user.name)
            tvProfileName.text = user.name
            tvProfileSpecialty.text = user.specialist
            edProfileEmail.setText(user.email)
            edProfileExperience.setText(user.experience)
            edHomePhone.setText(user.phone)
            edProfileDesc.setText(user.description)
        }
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data") as Bitmap
            mainViewModel.storeImage(imageBitmap,100)
        }
    }
    val documentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        val contentResolver = requireActivity().contentResolver
        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        mainViewModel.storeImage(imageBitmap, 100)
    }
    fun dispatchTakePictureIntent() {
        val pickImage = "Pick Image"
        val takePhoto = "Take Photo"

        val options = arrayOf<CharSequence>(pickImage, takePhoto)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Select Image Source")
        builder.setItems(options) { dialog, item ->
            when (options[item]) {
                pickImage -> {
                    if (ContextCompat.checkSelfPermission(requireActivity(), storagePermission) != PackageManager.PERMISSION_GRANTED && (ContextCompat.checkSelfPermission(requireActivity(),mediaPermission)) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(storagePermission,mediaPermission),
                            4
                        )
                    } else {
                        // Permission granted, launch file picker intent
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "image/*" // only allow image file types
                        documentLauncher.launch(intent.type)
                    }
                }
                takePhoto -> {
                    if (ContextCompat.checkSelfPermission(
                            requireActivity(),
                            cameraPermission
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // Permission not granted, request it
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(cameraPermission),
                            6
                        )
                    } else {
                        // Permission granted, launch camera intent
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        resultLauncher.launch(intent)
                    }
                }
            }
        }
        builder.show()
    }


}