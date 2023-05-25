package com.example.vetdoctorapp.controller.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetdoctorapp.model.data.Appointment
import com.example.vetdoctorapp.model.data.User
import com.example.vetdoctorapp.model.repositories.PatientRepository
import com.example.vetdoctorapp.model.repositories.UserRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(){

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    private val _userData = MutableLiveData<User?>()
    val userData: LiveData<User?> = _userData

    private val _appointmentList = MutableLiveData<List<Appointment>>()
    val appointmentList: LiveData<List<Appointment>> = _appointmentList

    init{
        getProfile()
    }

    fun getProfile(){
        UserRepository.getUserData(
            onSuccess = {
                _userData.value = it
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

    fun toggleActive(toggle: Boolean){
        UserRepository.getUserData(
            onSuccess = {
                val user = it
                if(user!=null)
                    user.available = toggle
                else return@getUserData
            }, onFailure = {
                _error.value = it
            }
        )
    }

    fun getPatients(){
        PatientRepository.getQueue(
            onSuccess = {
                _appointmentList.value = it
            },
            onFailure = {
                _error.value = it
            }
        )
    }


}