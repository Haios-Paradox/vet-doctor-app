package com.example.vetdoctorapp.view.auth.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.vetdoctorapp.R
import com.example.vetdoctorapp.controller.auth.AuthViewModel
import com.example.vetdoctorapp.databinding.ActivityAuthBinding
import com.example.vetdoctorapp.view.main.ui.MainActivity

class AuthActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding : ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        authViewModel.loggedInUser.observe(this){
            if(it==null)
                Navigation.findNavController(
                    this, binding.fragmentContainerView.id
                ).navigate(R.id.loginFragment)
            else
                startActivity(
                    Intent(this, MainActivity::class.java).apply {
                        addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                    Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    }
                )
        }

        authViewModel.error.observe(this){
            Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
        }

        setContentView(binding.root)
    }
}