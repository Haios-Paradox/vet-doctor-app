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

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    private val _userData = MutableLiveData<User?>()
    val userData: LiveData<User?> = _userData

    private val _appointmentList = MutableLiveData<List<DocumentSnapshot>>()
    val appointmentList: LiveData<List<DocumentSnapshot>> = _appointmentList

    private val _imageBitmap = MutableLiveData<Bitmap?>()
    val imageBitmap: LiveData<Bitmap?> = _imageBitmap

    init{
        getProfile()
        getPatients()
    }

    fun storeImage(bitmap: Bitmap, quality: Int) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val compressedBitmap =
            BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())
        _imageBitmap.value = compressedBitmap
    }

    private fun getProfile(){
        UserRepository.getUserData(
            onSuccess = {
                _userData.value = it
                getPatients()
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    fun updateProfile(user:User){
        viewModelScope.launch {
            try{
                UserRepository.createOrUpdateUserData(user)
            }catch (e:Exception){
                _error.value = e
            }
        }

    }

    fun updateProfile(user:User, file:Bitmap){
        viewModelScope.launch {
            try{
                UserRepository.createOrUpdateUserData(user,file)
            }catch (e:Exception){
                _error.value = e
            }
        }

    }

    private fun getPatients(){
        PatientRepository.getQueue(
            onSuccess = {
                _appointmentList.value = it
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    fun logout() {
        UserRepository.logout()
        _userData.value = null
    }

    fun endShift() {
        viewModelScope.launch {
            try {
                AppointmentRepository.endShift()
            }catch (e:Exception){
                _error.value = e
            }
        }

    }


}