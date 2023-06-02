package com.example.vetdoctorapp.model.data

import java.sql.Date

data class Patient(
    val name: String?=null,
    val email: String?=null,
    val phone: String?=null,
    val avatar: String?=null,
    val gender: String?=null,
    val desc: String?=null,
    val dob: Date?=null,
)
