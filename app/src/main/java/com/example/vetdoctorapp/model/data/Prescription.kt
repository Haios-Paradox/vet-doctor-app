package com.example.vetdoctorapp.model.data

data class Prescription(
    val appointmentId: String,
    val name: String, //Doctor's Name
    val analysis: String,
    val treatment: String
)
