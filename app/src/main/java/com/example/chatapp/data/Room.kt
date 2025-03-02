package com.example.chatapp.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

data class Room(
    val id: String = "",
    val name:String = ""
)

class RoomRepository(
    private val firestore: FirebaseFirestore
){
    suspend fun createRoom(name: String): Result<Unit>{
        return try {
            val room = Room(name = name)
            firestore.collection("rooms").add(room).await()
            Result.Success(Unit)
        }catch (e:Exception){
            Result.Error(e)
        }
    }

    suspend fun getRooms(): Result<List<Room>>{
        return try {
            val query = firestore.collection("rooms").get().await()
            val rooms = query.documents.map {
                documentSnapshot -> documentSnapshot.toObject(Room::class.java)!!.copy(id = documentSnapshot.id)
            }
            Result.Success(rooms)
        }catch (e:Exception){
            Result.Error(e)
        }
    }

}