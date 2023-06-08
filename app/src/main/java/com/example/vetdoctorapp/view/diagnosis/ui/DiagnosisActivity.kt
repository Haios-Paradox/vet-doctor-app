package com.example.vetdoctorapp.view.diagnosis.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.vetdoctorapp.R
import com.example.vetdoctorapp.controller.diagnosis.DiagnosisViewModel
import com.example.vetdoctorapp.controller.diagnosis.ViewModelFactory
import com.example.vetdoctorapp.databinding.ActivityDiagnosisBinding

class DiagnosisActivity : AppCompatActivity() {

    private lateinit var diagnosisViewModel: DiagnosisViewModel
    private lateinit var binding : ActivityDiagnosisBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appoId = intent.getStringExtra(APPO_ID)

        binding = ActivityDiagnosisBinding.inflate(layoutInflater)
        if (appoId != null) {
            diagnosisViewModel = ViewModelProvider(this, ViewModelFactory(appoId))[DiagnosisViewModel::class.java].also{
                it.message.observe(this){
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }
            setupNav()
        }else{
            finish()
        }
        setContentView(binding.root)
    }

    private fun setupNav() {
        binding.bottomNavigationView2.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_notes -> {
                    Navigation.findNavController(
                        this, binding.fragmentContainerView3.id
                    ).navigate(R.id.notesFragment)
                    true
                }
                R.id.navigation_chat ->{
                    Navigation.findNavController(
                        this, binding.fragmentContainerView3.id
                    ).navigate(R.id.chatFragment)
                    true
                }
                R.id.navigation_prescription -> {
                    Navigation.findNavController(
                        this, binding.fragmentContainerView3.id
                    ).navigate(R.id.prescriptionFragment)
                    true
                }
                else -> false
            }
        }
    }


    companion object{
        const val APPO_ID = "appo_id"
    }
}