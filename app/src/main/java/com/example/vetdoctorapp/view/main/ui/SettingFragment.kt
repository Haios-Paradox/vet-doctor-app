package com.example.vetdoctorapp.view.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vetdoctorapp.controller.main.MainViewModel
import com.example.vetdoctorapp.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        binding = FragmentSettingBinding.inflate(inflater,container,false)

        mainViewModel.userData.observe(requireActivity()){user->
            if(user!=null){
                binding.btnStartSetting.setOnClickListener {
                    user.available = true
                    user.limit = binding.edLimit.text.toString().toInt()
                    user.fee = binding.edFee.text.toString().toInt().toDouble()
                    mainViewModel.updateProfile(user)
                }
            }
        }

        return binding.root
    }


}