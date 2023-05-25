package com.example.vetdoctorapp.controller.diagnosis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetdoctorapp.model.data.Chat
import com.example.vetdoctorapp.model.data.Prescription
import com.example.vetdoctorapp.model.repositories.AppointmentRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.launch

class DiagnosisViewModel : ViewModel(){
    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    private val _chatData = MutableLiveData<List<Chat>>()
    val chatData: LiveData<List<Chat>> = _chatData

    private val _prescription = MutableLiveData<Prescription>()
    val prescription: LiveData<Prescription> = _prescription

    fun loadChats(appointmentId: String) {
        AppointmentRepository.getMessages(appointmentId) {
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

    fun getPrescription(appointmentId: String){
        AppointmentRepository.getPrescription(
            appointmentId,
            onSuccess = {
                _prescription.value = it
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