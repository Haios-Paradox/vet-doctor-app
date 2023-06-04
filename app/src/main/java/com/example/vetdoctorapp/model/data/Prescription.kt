package com.example.vetdoctorapp.model.data

data class Prescription(
    var appointmentId: String?=null,
    var name: String?=null, //Doctor's Name
    var analysis: String?=null,
    var treatment: String?=null
)
