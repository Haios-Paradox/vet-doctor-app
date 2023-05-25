package com.example.vetdoctorapp.model.data

data class Appointment(
    val id :String?=null,
    val patientId : String?=null,
    val doctorId : String?=null,
    val patientName: String?=null,
    val photo : String?=null,
    val description: String?=null,
    val prescription: Prescription?=null,
    val timestamp : Long?=null

)