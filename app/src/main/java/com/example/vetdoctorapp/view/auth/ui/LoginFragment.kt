package com.example.vetdoctorapp.view.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.vetdoctorapp.R
import com.example.vetdoctorapp.databinding.FragmentLoginBinding
import com.example.vetdoctorapp.view.auth.AuthViewModel


class LoginFragment : Fragment() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        binding.register.setOnClickListener {
            it.findNavController().navigate(R.id.registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.editText?.text.toString()
            val pass = binding.editTextPassword.editText?.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty()){
                authViewModel.login(email,pass)
            }
            else{
                Toast.makeText(requireActivity(),"Please Fill All In", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

}