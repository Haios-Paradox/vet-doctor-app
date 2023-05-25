package com.example.vetdoctorapp.view.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vetdoctorapp.R
import com.example.vetdoctorapp.controller.main.MainViewModel
import com.example.vetdoctorapp.databinding.FragmentProfileBinding
import com.example.vetdoctorapp.model.data.User

class ProfileFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        binding = FragmentProfileBinding.inflate(inflater,container,false)

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
            null,
            binding.edProfileName.text.toString(),
            binding.edProfileSpec.text.toString(),
            binding.edProfileEmail.text.toString(),
            binding.edHomePhone.text.toString(),
            binding.edProfileExperience.text.toString(),
            binding.edProfileDesc.text.toString()
        )
        mainViewModel.updateProfile(user)
    }

    private fun setupView(user: User) {
        with(binding){
           Glide.with(binding.imageView2).load(user.avatar?: R.drawable.ic_profile).into(binding.imageView2)
            edProfileName.setText(user.name)
            tvProfileName.text = user.name
            tvProfileSpecialty.text = user.specialist
            edProfileEmail.setText(user.email)
            edProfileExperience.setText(user.experience)
            edHomePhone.setText(user.phone)
            edProfileSpec.setText(user.specialist)
            edProfileDesc.setText(user.description)
        }
    }


}