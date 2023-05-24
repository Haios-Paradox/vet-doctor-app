package com.example.vetdoctorapp.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class References(auth:String) {
    private val PATIENT_COL= "patients"
    private val USER_COL = "doctors"
    private val APPOINT_COL = "appointments"
    private val COLLE_COL = "collection"

    private val db = Firebase.firestore

    val userDataRef = db.collection(USER_COL).document(auth)
    val patientColRef = db.collection(PATIENT_COL)
    val appointColRef = db.collection(APPOINT_COL)
    val specialistColRef = db.collection(COLLE_COL)

}