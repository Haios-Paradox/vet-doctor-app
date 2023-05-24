package com.example.vetdoctorapp.data

import com.example.vetdoctorapp.model.data.Chat
import com.example.vetdoctorapp.model.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class ChatRepository(auth: FirebaseAuth) {
    private val ref = auth.currentUser?.let { References(it.uid) }

    fun sendMessageToFirestore(
        user: User,
        message: String,
        appointmentId:String,
        onSuccess: (DocumentSnapshot)-> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (ref == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }
        val chatCollectionRef = ref.appointColRef.document(appointmentId).collection("chat")

        val chat = Chat(
            1,
            user.name,
            user.avatar,
            message,
        )
        chatCollectionRef.add(chat)
            .addOnSuccessListener{
                it.get().addOnSuccessListener {
                    onSuccess(it)
                }
            }
            .addOnFailureListener(onFailure)
    }

    fun loadChatMessages(
        appointmentId: String,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        if (ref == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }
        ref.appointColRef.document(appointmentId).collection("chat")
            .orderBy("timestamp").get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun observeIncomingMessages(
        appointmentId: String,
        onSuccess: (DocumentSnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (ref == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }
        val chatCollectionRef = ref.appointColRef.document(appointmentId).collection("chat")
        val query = chatCollectionRef
            .orderBy("timestamp", Query.Direction.ASCENDING)

        query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                onFailure(exception)
            }
            for (doc in snapshot?.documents!!) {
                onSuccess.invoke(doc)
            }

        }
    }
}