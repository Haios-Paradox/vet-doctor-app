package com.example.vetdoctorapp.model.repositories

import android.graphics.Bitmap
import com.example.vetdoctorapp.model.data.Appointment
import com.example.vetdoctorapp.model.data.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
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
    val userRef = db.collection(References.USER_COL)
    val currentDate = Date()

    fun getMessages(appointmentId: String, onMessagesChanged: (List<Chat>) -> Unit) : ListenerRegistration {
        val query = appointRef.document(appointmentId).collection(References.CHAT_COL).orderBy("timestamp")
        return query.addSnapshotListener { snapshots, error ->
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

    fun observeAppointmentDetail(
        appointmentId: String,
        onUpdate:(Appointment?)->Unit,
        onFailure:(Exception)->Unit
    ):ListenerRegistration {
        return appointRef.document(appointmentId)
            .addSnapshotListener { snapshot,e->
                if (e!=null){
                    onFailure(Exception(e))
                    return@addSnapshotListener
                }

                val appointment=snapshot?.toObject(Appointment::class.java)
                onUpdate(appointment)
            }
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

    suspend fun updateAppointment(
        appointment:Appointment,
    ) {
        appointRef.document(appointment.id!!).set(appointment).await()
    }

    fun endTreatment(
        appointmentId: String,
        onSuccess:(String)->Unit,
        onFailure:(Exception)->Unit)
    {
        appointRef.document(appointmentId).get().addOnSuccessListener {
            val appointment = it.toObject<Appointment>()
            appointment!!.complete = true
            userRef.document(auth.uid!!).update(
                "queue", FieldValue.arrayRemove(appointmentId)
            ).addOnSuccessListener{
                onSuccess("String")
            }.addOnFailureListener(onFailure)
        }.addOnFailureListener(onFailure)

    }
}