package com.example.vetdoctorapp.view.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vetdoctorapp.databinding.FragmentSettingBinding
import com.example.vetdoctorapp.view.main.MainViewModel


class SettingFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        binding = FragmentSettingBinding.inflate(inflater,container,false)

        mainViewModel.userData.observe(requireActivity()){

        }


        return binding.root
    }


}