package com.example.vetdoctorapp.controller.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetdoctorapp.model.data.User
import com.example.vetdoctorapp.model.repositories.AppointmentRepository
import com.example.vetdoctorapp.model.repositories.PatientRepository
import com.example.vetdoctorapp.model.repositories.UserRepository
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel(){

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

    private val _message = MutableLiveData<User?>()
    val message: LiveData<User?> = _message

    private val _appointmentList = MutableLiveData<List<DocumentSnapshot>>()
    val appointmentList: LiveData<List<DocumentSnapshot>> = _appointmentList

    private val _imageBitmap = MutableLiveData<Bitmap?>()
    val imageBitmap: LiveData<Bitmap?> = _imageBitmap

    val loading = MutableLiveData<Boolean>()

    init{
        getProfile()
    }

    fun storeImage(bitmap: Bitmap, quality: Int) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val compressedBitmap =
            BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())
        _imageBitmap.value = compressedBitmap
    }

    private fun getProfile(){
        loading.value = true
        UserRepository.getUserData(
            onSuccess = {
                _message.value = it
                getPatients()
                loading.value = false
            },
            onFailure = {e->
                _msg.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
            }
        )
    }

    fun updateProfile(user:User){
        viewModelScope.launch {
            loading.value = true
            try{
                UserRepository.createOrUpdateUserData(user)
                loading.value = false
                _msg.value = "Update Successful"
            }catch (e:Exception){
                loading.value = false
                _msg.value = e.cause?.message?:e.message?:"There was an error"
            }
        }

    }

    fun updateProfile(user:User, file:Bitmap){
        viewModelScope.launch {
            try{
                loading.value = true
                UserRepository.createOrUpdateUserData(user,file)
                loading.value = false
                _msg.value = "Update Successful"
            }catch (e:Exception){
                _msg.value = e.cause?.message?:e.message?:"There was an error"
            }
        }

    }

    private fun getPatients(){
        loading.value = true
        PatientRepository.getQueue(
            onSuccess = {
                loading.value = false
                _appointmentList.value = it
            },
            onFailure = {e->
                loading.value = false
                _msg.value = e.cause?.message?:e.message?:"There was an error"
            }
        )
    }

    fun logout() {
        UserRepository.logout()
        _message.value = null
    }

    fun endShift() {
        viewModelScope.launch {
            try {
                loading.value = true
                AppointmentRepository.endShift()
                loading.value = false
                _msg.value = "Shift Ended!"
            }catch (e:Exception){
                _msg.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
            }
        }

    }


}