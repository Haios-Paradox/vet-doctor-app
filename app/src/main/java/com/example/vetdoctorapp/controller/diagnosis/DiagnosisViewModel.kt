package com.example.vetdoctorapp.controller.diagnosis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetdoctorapp.model.data.Appointment
import com.example.vetdoctorapp.model.data.Chat
import com.example.vetdoctorapp.model.data.Prescription
import com.example.vetdoctorapp.model.repositories.AppointmentRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class DiagnosisViewModel : ViewModel(){
    var appointmentReg : ListenerRegistration? = null
    var chatReg : ListenerRegistration? =null

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    private val _chatData = MutableLiveData<List<Chat>>()
    val chatData: LiveData<List<Chat>> = _chatData

    private val _appointment = MutableLiveData<Appointment>()
    val appointment: LiveData<Appointment> = _appointment

    fun loadChats(appointmentId: String) {
        chatReg = AppointmentRepository.getMessages(appointmentId) {
            _chatData.value = it
        }
    }

    fun sendChat(appointmentId: String, message: Chat){
        viewModelScope.launch {
            try {
                AppointmentRepository.sendMessage(appointmentId,message)
            }catch (e: FirebaseFirestoreException) {
                _error.value = e
            }
        }

    }

    fun getAppointment(appointmentId: String){
        appointmentReg = AppointmentRepository.observeAppointmentDetail(
            appointmentId,
            onUpdate = {
                _appointment.value = it
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    suspend fun updatePrescription(appointmentId: String, prescription:Prescription){
        viewModelScope.launch {
            try{
                AppointmentRepository.updatePrescription(appointmentId,prescription)
            }catch (e:Exception){
                _error.value = e
            }
        }

    }
}