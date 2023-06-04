package com.example.vetdoctorapp.controller.diagnosis

import androidx.lifecycle.*
import com.example.vetdoctorapp.model.data.Appointment
import com.example.vetdoctorapp.model.data.Chat
import com.example.vetdoctorapp.model.data.User
import com.example.vetdoctorapp.model.repositories.AppointmentRepository
import com.example.vetdoctorapp.model.repositories.UserRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class DiagnosisViewModel(val appointmentId: String) : ViewModel(){
    var appointmentReg : ListenerRegistration? = null
    var chatReg : ListenerRegistration? =null

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    private val _chatData = MutableLiveData<List<Chat>>()
    val chatData: LiveData<List<Chat>> = _chatData

    private val _appointment = MutableLiveData<Appointment>()
    val appointment: LiveData<Appointment> = _appointment

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    init{
        getAppointment()
        loadChats()
        getUser()
    }

    fun getUser(){
        UserRepository.getUserData(
            onSuccess = {
                _user.value = it
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    fun loadChats() {
        chatReg = AppointmentRepository.getMessages(appointmentId) {
            _chatData.value = it
        }
    }

    fun sendChat(message: Chat){
        viewModelScope.launch {
            try {
                AppointmentRepository.sendMessage(appointmentId,message)
            }catch (e: FirebaseFirestoreException) {
                _error.value = e
            }
        }

    }

    fun getAppointment(){
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

    fun updatePrescription(analysis:String, treatment:String){
        viewModelScope.launch {
            try{
                val appointment = appointment.value
                appointment!!.analysis = analysis
                appointment.treatment = treatment
                AppointmentRepository.updateAppointment(appointment)
            }catch (e:Exception){
                _error.value = e
            }
        }

    }

    fun endCheckUp(analysis:String, treatment:String){
        updatePrescription(analysis, treatment)
        AppointmentRepository.endTreatment(
            appointmentId,
            onSuccess = {
                getAppointment()
            },
            onFailure = {

            }
        )
    }
}

class ViewModelFactory(private val appointmentId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiagnosisViewModel::class.java)) {
            return DiagnosisViewModel(appointmentId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}