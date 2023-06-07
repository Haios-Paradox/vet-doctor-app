package com.example.vetdoctorapp.model.data

data class User(
    var id: String?=null,
    var avatar: String ?=null,
    val name : String?=null,
    val specialist: String? =null,
    val email: String?=null,
    val phone: String?=null,
    val experience: String?=null,
    val description: String?=null,
    var fee: Double?=null,
    var limit: Int?=0,
    var available : Boolean?=false,
    val queue: List<String>?= emptyList(),
    val finished_queue: List<String>?= emptyList()
)
