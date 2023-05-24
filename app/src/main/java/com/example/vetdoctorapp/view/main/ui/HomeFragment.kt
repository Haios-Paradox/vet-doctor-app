package com.example.vetdoctorapp.view.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vetdoctorapp.databinding.FragmentHomeBinding
import com.example.vetdoctorapp.view.main.MainViewModel

class HomeFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        binding = FragmentHomeBinding.inflate(inflater,container,false)


        return binding.root
    }



}