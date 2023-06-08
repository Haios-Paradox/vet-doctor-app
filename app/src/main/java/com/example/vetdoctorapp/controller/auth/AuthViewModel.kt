package com.example.vetdoctorapp.controller.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vetdoctorapp.model.data.User
import com.example.vetdoctorapp.model.repositories.AuthRepository

class AuthViewModel: ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _loggedInUser = MutableLiveData<String?>()
    val loggedInUser: MutableLiveData<String?> = _loggedInUser

    val loading = MutableLiveData<Boolean>()

    init{
        autoLogin()
    }

    private fun autoLogin() {
        _loggedInUser.value = AuthRepository.getUser()
    }

    fun login(email:String, pass:String){
        loading.value = true
        AuthRepository.login(
            email,pass,
            onSuccess = {
                autoLogin()
                loading.value = false
            },
            onFailure = {e->
                _message.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
            }
        )
    }

    fun register(email:String, pass:String, userData: User){
        loading.value = true
        AuthRepository.register(userData,email,pass){_, e ->
            if(e==null) {
                login(email, pass)
                loading.value = false
            }
            else {
                _message.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
            }
        }
    }
}