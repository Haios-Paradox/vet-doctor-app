package com.example.vetdoctorapp.model.data

data class Chat(
    val chatId: String?= null,
    val senderType: Int?= null, //Patient =0, Doctor =1
    val name: String?= null,
    val avatar: String ?= null,
    val message: String ?= null,
    val sender: String?= null,
    val content: String?= null,
    val timestamp: Long?= null,
)
