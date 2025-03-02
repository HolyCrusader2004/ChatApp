package com.example.chatapp.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

data class Message(val text: String = "",
    val senderFirstName: String = "",
    val senderId: String = "",
    val isSentByCurrentUser: Boolean = false,
    val timeStamp:Long = System.currentTimeMillis())

class MessageRepository(private val firestore: FirebaseFirestore){

    suspend fun sendMessage(roomId:String, message: Message): Result<Unit>{
        return try {
            firestore.collection("rooms").document(roomId).collection("messages").add(message).await()
            Result.Success(Unit)
        }catch (e: Exception){
            Result.Error(e)
        }
    }

    fun getChatMessages(roomId: String): Flow<List<Message>> =
        callbackFlow {
            val subscription = firestore.collection("rooms").document(roomId)
                .collection("messages")
                .orderBy("timeStamp")
                .addSnapshotListener{
                    querySnapshot, _ ->
                    querySnapshot?.let {
                        trySend(it.documents.map {
                            documentSnapshot -> documentSnapshot.toObject(Message::class.java)!!.copy()
                        }).isSuccess
                    }
                }
            awaitClose{subscription.remove()}
        }
    }
