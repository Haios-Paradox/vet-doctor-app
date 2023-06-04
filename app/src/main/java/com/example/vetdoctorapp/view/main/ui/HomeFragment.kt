package com.example.vetdoctorapp.view.main.ui

import android.content.Intent
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
import com.example.vetdoctorapp.view.diagnosis.ui.DiagnosisActivity

class HomeFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.appointmentList.observe(requireActivity()) { data ->
            val appointments: MutableMap<String, Appointment> = mutableMapOf()
            data.forEach { documentSnapshot ->
                val appointment =
                    documentSnapshot.toObject(Appointment::class.java)!!
                appointments[documentSnapshot.id] = appointment
            }

            val sortedAppointments = mutableMapOf<String, Appointment>()
            appointments.toSortedMap(compareByDescending { appointments[it]!!.timestamp }).forEach {
                sortedAppointments[it.key] = it.value
            }

            setupView(sortedAppointments[sortedAppointments.keys.last()]!!, sortedAppointments.keys.last())

        }

        return binding.root
    }

    private fun setupView(appointment: Appointment, appoId:String) {
        binding.tvHomePatientName.text = appointment.patientName
        binding.tvHomePatientDesc.text = appointment.description
        Glide.with(binding.tvHomePatientPhoto).load(appointment.photo).into(binding.tvHomePatientPhoto)
        binding.tvHomeCurrent.text = "Current Patient"
        binding.btnCheckPatient.setOnClickListener {
            startActivity(
                Intent(requireActivity(), DiagnosisActivity::class.java)
                    .putExtra(DiagnosisActivity.APPO_ID, appoId)
            )
        }
    }


}