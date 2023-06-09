package com.example.vetdoctorapp.model.repositories

import android.graphics.Bitmap
import com.example.vetdoctorapp.model.data.Chat
import com.example.vetdoctorapp.model.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream


object UserRepository{
    private val auth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    private val storage = Firebase.storage
    private val storageRef = storage.reference
    fun getUserData(
        onSuccess: (User?) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        val uid = auth.uid
        if (uid == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }
        db.collection(References.USER_COL).document(uid)
            .addSnapshotListener {snapshot,error ->
                val user = snapshot?.toObject<User>()
                onSuccess(user)
            }
    }

    fun getMessages(appointmentId: String, onMessagesChanged: (List<Chat>) -> Unit) : ListenerRegistration {
        val query = AppointmentRepository.appointRef.document(appointmentId).collection(References.CHAT_COL).orderBy("timestamp")
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

    suspend fun createOrUpdateUserData(
        userData: User,
    ){
        val uid = auth.uid ?: throw (Exception("User Not Logged In"))
        db.collection(References.USER_COL).document(uid).set(userData).await()
    }
    suspend fun createOrUpdateUserData(
        userData: User, file: Bitmap,
    ) {
        val uid = auth.uid ?: throw (Exception("User Not Logged In"))
        val avatar = uploadImage(file,uid)
        userData.avatar = avatar
        db.collection(References.USER_COL).document(uid).set(userData).await()
    }

    suspend fun uploadImage(image: Bitmap, fileName: String): String {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val path = "UserData/Avatar/$fileName.jpg"
        val imageRef = storageRef.child(path)
        return try {
            imageRef.putBytes(data).await()
            val downloadUrl = imageRef.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            throw e
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun resetPassword(
        email:String,
        onSuccess: (String?) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess("Email Sent")
                }else{
                    onFailure(Exception("It Just Don't Work IDK Why"))
                }
            }.addOnFailureListener(onFailure)
    }
}