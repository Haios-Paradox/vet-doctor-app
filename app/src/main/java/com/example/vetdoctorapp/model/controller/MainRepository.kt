package com.example.vetdoctorapp.model.controller

import android.content.Context
import com.example.vetdoctorapp.data.AppointmentRepository
import com.example.vetdoctorapp.data.ChatRepository
import com.example.vetdoctorapp.data.UserRepository
import com.example.vetdoctorapp.model.data.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class MainRepository(
    private val appointment: AppointmentRepository,
    private val user: UserRepository,
    private val chat: ChatRepository,
    private val auth : FirebaseAuth,
) {

    val uid = auth.currentUser?.uid

    fun getUser(): FirebaseUser? {
        return auth.currentUser
    }

    /**
     * Login function...
     * @param email is self explanatory
     * @param password is also self explanatory
     * @param onSuccess returns auth result if successful
     * @param onFailure returns an exception if anything went wrong
     */
    fun login(
        email : String,
        password: String,
        onSuccess: (AuthResult) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    /**
     * Function to register.
     * @param email is self explanatory
     * @param password is also self explanatory
     * @param userData is the user data sent during registration
     * @param onSuccess returns the newly created user document
     * @param onFailure returns an exception if anything went wrong
     */
    fun register(
        email : String,
        password: String,
        userData: User,
        onSuccess: (Void) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnSuccessListener {
                        user.createOrUpdateUserData(
                            userData,
                            onSuccess,
                            onFailure
                        )
                }.addOnFailureListener(onFailure)
            }.addOnFailureListener(onFailure)
    }

    /**
     * Function to Update User Data
     * Because of some firebase witchcraft, the function to create and
     * update the user data is the exact same.
     * @param userData is the Data you want to create/update
     * @param onSuccess is the result of the update
     * @param onFailure is the exception if anything went wrong
     */
    fun updateUserData(
        userData: User,
        onSuccess: (Void) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        user.createOrUpdateUserData(userData,onSuccess,onFailure)
    }

    fun getUserData(
        onSuccess: (DocumentSnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        user.getUserData(onSuccess,onFailure)
    }

    fun loadChat(
        appointId: String,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        chat.loadChatMessages(
            appointId, onSuccess, onFailure
        )
    }

    fun observeChat(
        appointId: String,
        onSuccess: (DocumentSnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        chat.observeIncomingMessages(
            appointId,onSuccess,onFailure
        )
    }

    fun sendMessage(
        user:User,
        appointId: String,
        message:String,
        onSuccess: (DocumentSnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        chat.sendMessageToFirestore(
            user, message,appointId, onSuccess, onFailure
        )
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null

        fun getInstance(context: Context): MainRepository {
            return instance ?: synchronized(MainRepository::class.java) {

                val auth = FirebaseAuth.getInstance()

                if (instance == null) {
                    val appointment = AppointmentRepository(auth)
                    val user = UserRepository(auth)
                    val chat = ChatRepository(auth)
                    instance = MainRepository(appointment,user, chat, auth)
                }
                return instance as MainRepository
            }
        }
    }
}