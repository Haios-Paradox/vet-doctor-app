package com.example.vetdoctorapp.model.repositories

import android.graphics.Bitmap
import com.example.vetdoctorapp.model.data.Chat
import com.example.vetdoctorapp.model.data.Prescription
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*

object AppointmentRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    val appointRef = db.collection(References.APPOINT_COL)
    val currentDate = Date()

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

    suspend fun sendMessage(
        appointmentId: String, message: Chat, file:Bitmap
    ){
        val image = sendImage(file,appointmentId)
        message.content = image
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

    private suspend fun sendImage(image: Bitmap, appointmentId: String): String {
        val uid = auth.uid ?: throw (Exception("User Not Logged In"))
        val time = currentDate.time.toString()
        return uploadImage(image,"$uid$time",appointmentId)
    }

    private suspend fun uploadImage(image: Bitmap, fileName: String, appointmentId: String): String {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val path = "UserData/Appointment/$appointmentId/$fileName.jpg"
        val imageRef = storageRef.child(path)
        return try {
            imageRef.putBytes(data).await()
            val downloadUrl = imageRef.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updatePrescription(
        appointmentId: String,
        prescription:Prescription,
    ) {
        appointRef.document(appointmentId).set(prescription).await()
    }
}