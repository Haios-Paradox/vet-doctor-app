package com.example.vetdoctorapp.model.repositories

import com.example.vetdoctorapp.model.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object UserRepository{
    private val auth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }
    fun getUserData(
        onSuccess: (User?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val uid = auth.uid
        if (uid == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }
        db.collection(References.USER_COL).document(uid).get()
            .addOnSuccessListener {
                val user = it.toObject<User>()
                onSuccess(user)
            }
            .addOnFailureListener(onFailure)
    }

    suspend fun createOrUpdateUserData(
        userData: User,
    ){
        val uid = auth.uid ?: throw (Exception("User Not Logged In"))
        db.collection(References.USER_COL).document(uid).set(userData).await()
    }
}