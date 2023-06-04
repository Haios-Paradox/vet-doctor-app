package com.example.vetdoctorapp.view.diagnosis.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vetdoctorapp.controller.diagnosis.ChatAdapter
import com.example.vetdoctorapp.controller.diagnosis.DiagnosisViewModel
import com.example.vetdoctorapp.databinding.FragmentChatBinding
import com.example.vetdoctorapp.model.data.Chat
import java.util.*


class ChatFragment : Fragment() {

    private lateinit var diagnosisViewModel: DiagnosisViewModel
    private lateinit var binding : FragmentChatBinding
    private lateinit var adapter : ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        diagnosisViewModel = ViewModelProvider(requireActivity())[DiagnosisViewModel::class.java]
        binding = FragmentChatBinding.inflate(inflater,container,false)
        val rvLayout = LinearLayoutManager(requireActivity())
        rvLayout.reverseLayout = true
        binding.rvChat.layoutManager = rvLayout
        diagnosisViewModel.chatData.observe(requireActivity()){
            adapter = ChatAdapter(it.sortedBy { it.timestamp }.reversed())
            binding.rvChat.adapter = adapter
        }

        diagnosisViewModel.user.observe(requireActivity()){user->
            binding.btnSend.setOnClickListener {
                val chat = Chat(
                    null,
                    1,
                    user.name,
                    user.avatar,
                    binding.textSend.text.toString(),
                    null,
                    null,
                    Date().time
                )
                diagnosisViewModel.sendChat(chat)
                binding.textSend.setText("")
            }
        }
        return binding.root
    }

}