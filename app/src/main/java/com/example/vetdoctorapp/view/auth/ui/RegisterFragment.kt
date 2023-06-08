package com.example.vetdoctorapp.view.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.vetdoctorapp.R
import com.example.vetdoctorapp.controller.auth.AuthViewModel
import com.example.vetdoctorapp.databinding.FragmentRegisterBinding
import com.example.vetdoctorapp.model.data.User

class RegisterFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding : FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java].also {
        }
        binding = FragmentRegisterBinding.inflate(inflater,container,false)

        binding.buttonRegister.setOnClickListener {
            val name = binding.editNameRegister.text.toString()
            val email = binding.editEmailRegister.text.toString()
            val pass = binding.editPassRegister.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty() && name.isNotEmpty()){
                authViewModel.register(email,pass, User(name = name, email = email))
            }
            else{
                Toast.makeText(requireActivity(),"Please Fill All In", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        return binding.root
    }

}