package com.example.vetdoctorapp.model.data

data class Appointment(
    var id :String?=null,
    val patientId : String?=null,
    val patientName: String?=null,
    val doctorId:String?=null,
    val doctorName:String?=null,
    var photo : String?=null,
    val description: String?=null,
    var analysis:String?=null,
    var treatment:String?=null,
    val timestamp : Long?=null,
    var complete : Boolean?=false,
    var paid : Boolean?=false,
    var payment: String?=null
)