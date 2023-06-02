package com.example.vetdoctorapp.view.diagnosis.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vetdoctorapp.controller.diagnosis.DiagnosisViewModel
import com.example.vetdoctorapp.databinding.ActivityDiagnosisBinding

class DiagnosisActivity : AppCompatActivity() {

    private lateinit var diagnosisViewModel: DiagnosisViewModel
    private lateinit var binding : ActivityDiagnosisBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appoId = intent.getStringExtra(APPO_ID)
        diagnosisViewModel = ViewModelProvider(this)[DiagnosisViewModel::class.java]
        binding = ActivityDiagnosisBinding.inflate(layoutInflater)
        if (appoId != null) {
            diagnosisViewModel.getAppointment(appoId)
            diagnosisViewModel.loadChats(appoId)
        }else{
            finish()
        }
        setContentView(binding.root)

    }

    companion object{
        const val APPO_ID = "appo_id"
    }
}