package com.example.vetdoctorapp.view.main.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.vetdoctorapp.R
import com.example.vetdoctorapp.controller.main.MainViewModel
import com.example.vetdoctorapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)

        mainViewModel.error.observe(this){
            Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
        }

        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_profile -> {
                    Navigation.findNavController(
                        this, binding.fragmentContainerView2.id
                    ).navigate(R.id.profileFragment)
                    true
                }
                R.id.navigation_doctors ->{
                    if(mainViewModel.userData.value?.available==true)
                        Navigation.findNavController(
                            this, binding.fragmentContainerView2.id
                        ).navigate(R.id.homeFragment)
                    else
                        Navigation.findNavController(
                            this, binding.fragmentContainerView2.id
                        ).navigate(R.id.settingFragment)
                    true
                }
                R.id.navigation_history -> {
                    Navigation.findNavController(
                        this, binding.fragmentContainerView2.id
                    ).navigate(R.id.appointmentListFragment)
                    true
                }
                else -> false
            }
        }
    }
}