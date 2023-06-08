package com.example.vetdoctorapp.view.main.ui

import android.content.Intent
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
import com.example.vetdoctorapp.model.data.Appointment
import com.example.vetdoctorapp.view.diagnosis.ui.DiagnosisActivity

class AppointmentListFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : FragmentAppointmentListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAppointmentListBinding.inflate(inflater,container,false)
        binding.rvAppointment.layoutManager = LinearLayoutManager(requireActivity())
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java].also {
            it.loading.observe(requireActivity()) {
                if (it)
                    binding.progressBar.visibility = View.GONE
                else
                    binding.progressBar.visibility = View.VISIBLE
            }
        }
        mainViewModel.appointmentList.observe(requireActivity()){data->
            val adapter = AppointmentAdapter(data)
            binding.rvAppointment.adapter = adapter
            adapter.setOnItemClickCallback(object : AppointmentAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Appointment) {
                    startActivity(
                        Intent(requireActivity(), DiagnosisActivity::class.java)
                            .putExtra(DiagnosisActivity.APPO_ID, data.id)
                    )
                }

            })
        }

        return binding.root
    }


}