package com.example.vetdoctorapp.controller.diagnosis

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.*
import com.example.vetdoctorapp.model.data.Appointment
import com.example.vetdoctorapp.model.data.Chat
import com.example.vetdoctorapp.model.data.User
import com.example.vetdoctorapp.model.repositories.AppointmentRepository
import com.example.vetdoctorapp.model.repositories.UserRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class DiagnosisViewModel(val appointmentId: String) : ViewModel(){
    var appointmentReg : ListenerRegistration? = null
    var chatReg : ListenerRegistration? =null

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _chatData = MutableLiveData<List<Chat>>()
    val chatData: LiveData<List<Chat>> = _chatData

    private val _appointment = MutableLiveData<Appointment>()
    val appointment: LiveData<Appointment> = _appointment

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _imageBitmap = MutableLiveData<Bitmap?>()
    val imageBitmap: LiveData<Bitmap?> = _imageBitmap

    val finished = MutableLiveData<Boolean>()

    val loading = MutableLiveData<Boolean>()

    init{
        getAppointment()
        loadChats()
        getUser()
    }

    fun getUser(){
        loading.value = true
        UserRepository.getUserData(
            onSuccess = {
                _user.value = it
                loading.value = false
            },
            onFailure = {e->
                _message.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
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
                if(imageBitmap.value==null)
                    AppointmentRepository.sendMessage(appointmentId,message)
                else
                    AppointmentRepository.sendMessage(appointmentId,message, imageBitmap.value!!)
                _imageBitmap.value = null
            }catch (e: FirebaseFirestoreException) {
                _message.value = e.cause?.message?:e.message?:"There was an error"
            }
        }
    }


    fun getAppointment(){
        loading.value = true
        appointmentReg = AppointmentRepository.observeAppointmentDetail(
            appointmentId,
            onUpdate = {
                _appointment.value = it
                loading.value = false
            },
            onFailure = {e->
                _message.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
            }
        )
    }

    fun updatePrescription(analysis:String, treatment:String){
        viewModelScope.launch {
            try{
                loading.value = true
                val appointment = appointment.value
                appointment!!.analysis = analysis
                appointment.treatment = treatment
                AppointmentRepository.updateAppointment(appointment)
                loading.value = false
                _message.value = "Updated!"
            }catch (e:Exception){
                _message.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
            }
        }

    }

    fun endCheckUp(analysis:String, treatment:String){
        loading.value = true
        updatePrescription(analysis, treatment)
        AppointmentRepository.endTreatment(
            appointmentId,
            onSuccess = {
                finished.value = true
                loading.value = false
            },
            onFailure = {e->
                loading.value = false
                _message.value = e.cause?.message?:e.message?:"There was an error"
            }
        )
    }

    fun storeImage(bitmap: Bitmap, quality: Int) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val compressedBitmap =
            BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())
        _imageBitmap.value = compressedBitmap
    }

    fun approve() {
        viewModelScope.launch {
            try{
                val approved = appointment.value
                if (approved != null) {
                    approved.paid = true
                    AppointmentRepository.updateAppointment(approved)
                }
                _message.value = "Payment Approved"
            }catch(e:Exception){
                _message.value = e.cause?.message?:e.message?:"There was an error"
            }
        }

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