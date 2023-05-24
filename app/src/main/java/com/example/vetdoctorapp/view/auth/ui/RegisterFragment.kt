package com.example.vetdoctorapp.view.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vetdoctorapp.databinding.FragmentRegisterBinding
import com.example.vetdoctorapp.model.data.User
import com.example.vetdoctorapp.view.auth.AuthViewModel

class RegisterFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding : FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding = FragmentRegisterBinding.inflate(inflater,container,false)

        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextName.editText?.text.toString()
            val email = binding.editTextEmail.editText?.text.toString()
            val pass = binding.editTextPassword.editText?.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty() && name.isNotEmpty()){
                authViewModel.register(email,pass, User(name = name))
            }
            else{
                Toast.makeText(requireActivity(),"Please Fill All In", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

}