package com.example.vetdoctorapp.view.diagnosis.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vetdoctorapp.controller.diagnosis.DiagnosisViewModel
import com.example.vetdoctorapp.databinding.FragmentPrescriptionBinding

class PrescriptionFragment : Fragment() {

    private lateinit var diagnosisViewModel: DiagnosisViewModel
    private lateinit var binding : FragmentPrescriptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        diagnosisViewModel = ViewModelProvider(requireActivity())[DiagnosisViewModel::class.java]
        binding = FragmentPrescriptionBinding.inflate(inflater,container,false)

        binding.btnPresSave.setOnClickListener {
            val analysis = binding.edAnalysis.text.toString()
            val treatment = binding.edTreatment.text.toString()
            diagnosisViewModel.updatePrescription(analysis,treatment)
        }

        binding.btnEnd.setOnClickListener {
            diagnosisViewModel.endCheckUp(
                binding.edAnalysis.text.toString(),
                binding.edTreatment.text.toString()
            )
            requireActivity().finish()
        }
        return binding.root
    }


}