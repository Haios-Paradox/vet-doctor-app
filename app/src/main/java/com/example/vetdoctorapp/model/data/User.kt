package com.example.vetdoctorapp.model.data

import java.sql.Timestamp

data class User(
    val avatar: String ?=null,
    val name : String?=null,
    val specialist: String? =null,
    val email: String?=null,
    val phone: String?=null,
    val experience: String?=null,
    val fee: Double?=null,
    val limit: Int?=null,
    val queue: List<Queue>?= emptyList(),
)

data class Queue(
    val patientId : String ?=null,
    val timestamp: Timestamp?=null
)
