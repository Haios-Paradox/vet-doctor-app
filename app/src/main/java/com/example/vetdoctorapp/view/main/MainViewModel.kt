package com.example.vetdoctorapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vetdoctorapp.model.controller.MainRepository
import com.example.vetdoctorapp.model.data.User
import com.google.firebase.firestore.ktx.toObject

class MainViewModel(private val mainRepository: MainRepository) : ViewModel(){

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> = _userData

    init{
        getProfile()
    }

    fun getProfile(){
        mainRepository.getUserData(
            onSuccess = {
                _userData.value = it.toObject<User>()
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    fun updateProfile(user:User){
        mainRepository.updateUserData(
            user,
            onSuccess = {},
            onFailure = {_error.value = it}
        )
    }

    fun getPatients(){
    }
}