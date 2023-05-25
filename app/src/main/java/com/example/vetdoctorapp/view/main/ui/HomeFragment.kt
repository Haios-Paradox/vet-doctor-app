package com.example.vetdoctorapp.view.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vetdoctorapp.controller.main.MainViewModel
import com.example.vetdoctorapp.databinding.FragmentHomeBinding
import com.example.vetdoctorapp.model.data.Appointment

class HomeFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.appointmentList.observe(requireActivity()){data->
            val appointments=data.sortedBy{it.timestamp}
            setupView(appointments[0])
        }

        return binding.root
    }

    private fun setupView(appointment: Appointment) {
        binding.tvHomePatientName.text = appointment.patientName
        binding.tvHomePatientDesc.text = appointment.description
        Glide.with(binding.tvHomePatientPhoto).load(appointment.photo).into(binding.tvHomePatientPhoto)
        binding.tvHomeCurrent.text = "Current Patient"
    }


}