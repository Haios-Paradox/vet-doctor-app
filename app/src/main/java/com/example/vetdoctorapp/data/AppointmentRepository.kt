package com.example.vetdoctorapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AppointmentRepository(auth:FirebaseAuth) {
    private val ref = auth.currentUser?.let { References(it.uid) }

}