package com.example.vetdoctorapp.model.repositories

import com.example.vetdoctorapp.model.data.Chat
import com.example.vetdoctorapp.model.data.Prescription
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object AppointmentRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }
    val appointRef = db.collection(References.APPOINT_COL)

    fun getMessages(appointmentId: String, onMessagesChanged: (List<Chat>) -> Unit) {
        val query = appointRef.document(appointmentId).collection(References.CHAT_COL).orderBy("timestamp")
        query.addSnapshotListener { snapshots, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            val messages = mutableListOf<Chat>()
            if (snapshots != null) {
                for (doc in snapshots) {
                    messages.add(doc.toObject())
                }
            }
            onMessagesChanged(messages)
        }
    }

    suspend fun sendMessage(
        appointmentId:String, message: Chat
    ) {
        appointRef.document(appointmentId).collection(References.CHAT_COL).add(message).await()
    }

    fun getPrescription(
        appointmentId: String,
        onSuccess:(Prescription?)->Unit,
        onFailure:(Exception)->Unit
    ){
        appointRef.document(appointmentId).get()
            .addOnSuccessListener{
                onSuccess(it.toObject())
            }.addOnFailureListener(onFailure)
    }

    suspend fun updatePrescription(
        appointmentId: String,
        prescription:Prescription,
    ) {
        appointRef.document(appointmentId).set(prescription).await()
    }
}