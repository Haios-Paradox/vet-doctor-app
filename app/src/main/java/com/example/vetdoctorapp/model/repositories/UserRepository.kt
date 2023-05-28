package com.example.vetdoctorapp.model.repositories

import android.graphics.Bitmap
import com.example.vetdoctorapp.model.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        userData: User
    ){
        val uid = auth.uid ?: throw (Exception("User Not Logged In"))
        db.collection(References.USER_COL).document(uid).set(userData).await()
    }
    suspend fun createOrUpdateUserData(
        userData: User, file:Bitmap
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
}