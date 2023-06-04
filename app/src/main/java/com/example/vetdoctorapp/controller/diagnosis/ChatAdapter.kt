package com.example.vetdoctorapp.controller.diagnosis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vetdoctorapp.databinding.ItemRowChatBinding
import com.example.vetdoctorapp.model.data.Chat

class ChatAdapter(private val messages: List<Chat>) :
    RecyclerView.Adapter<MessageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemRowChatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        with(holder){
            with(binding){
                Glide.with(ivSender).load(messages[position].avatar).into(ivSender)
                tvSenderName.text = messages[position].name
                tvSenderContent.text = messages[position].message
            }
        }
    }


}

class MessageViewHolder(
    val binding : ItemRowChatBinding
    ): RecyclerView.ViewHolder(binding.root)