package com.example.vetdoctorapp.view.diagnosis.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vetdoctorapp.controller.diagnosis.DiagnosisViewModel
import com.example.vetdoctorapp.databinding.FragmentNotesBinding


class NotesFragment : Fragment() {

    private lateinit var diagnosisViewModel: DiagnosisViewModel
    private lateinit var binding : FragmentNotesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        diagnosisViewModel = ViewModelProvider(requireActivity())[DiagnosisViewModel::class.java]
        binding = FragmentNotesBinding.inflate(inflater,container,false)

        diagnosisViewModel.appointment.observe(requireActivity()){
            Glide.with(binding.ivPetImage).load(it.photo).into(binding.ivPetImage)
            binding.tvNotesPatient.text = it.patientName
            binding.tvNotesProblem.text = it.description
        }

        return binding.root
    }

}