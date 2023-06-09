package com.example.vetdoctorapp.view.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vetdoctorapp.controller.main.MainViewModel
import com.example.vetdoctorapp.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingBinding.inflate(inflater,container,false)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java].also {
            it.loading.observe(requireActivity()) {
                if (!it)
                    binding.progressBar7.visibility = View.GONE
                else
                    binding.progressBar7.visibility = View.VISIBLE
            }
        }


        mainViewModel.userData.observe(requireActivity()){ user->
            if(user!=null){
                binding.btnStartSetting.setOnClickListener {
                    if(
                        user.experience!=null&&
                        user.avatar!=null&&
                        user.description!=null&&
                        user.name!=null&&
                        user.phone!=null
                    ) {
                        val limit = binding.edLimit.text.toString()
                        val fee = binding.edFee.text.toString()
                        if(fee.isNotEmpty() && limit.isNotEmpty()){
                            user.limit = limit.toInt()
                            user.fee = fee.toInt().toDouble()

                            user.available = true

                            mainViewModel.updateProfile(user)
                        }
                        else{
                            Toast.makeText(requireActivity(), "Please Fill In Fee and Limit", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else
                        Toast.makeText(requireActivity(),"Please Complete Profile First",Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }


}