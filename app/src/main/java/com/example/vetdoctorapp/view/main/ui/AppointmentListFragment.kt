package com.example.vetdoctorapp.view.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vetdoctorapp.controller.main.AppointmentAdapter
import com.example.vetdoctorapp.controller.main.MainViewModel
import com.example.vetdoctorapp.databinding.FragmentAppointmentListBinding

class AppointmentListFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : FragmentAppointmentListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAppointmentListBinding.inflate(inflater,container,false)
        binding.rvAppointment.layoutManager = LinearLayoutManager(requireActivity())
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.appointmentList.observe(requireActivity()){data->
            binding.rvAppointment.adapter = AppointmentAdapter(data)
        }

        return binding.root
    }


}