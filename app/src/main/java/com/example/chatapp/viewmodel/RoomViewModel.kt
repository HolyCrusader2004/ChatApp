package com.example.chatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.Injection
import com.example.chatapp.data.Result
import com.example.chatapp.data.Room
import com.example.chatapp.data.RoomRepository
import kotlinx.coroutines.launch

class RoomViewModel:ViewModel() {
    private val _rooms = MutableLiveData<List<Room>>()
    val rooms = _rooms
    private val roomRepository:RoomRepository

    init {
        roomRepository = RoomRepository(Injection.instance())
        getRooms()
    }

    fun createRoom(name:String){
        viewModelScope.launch {
            roomRepository.createRoom(name)
        }
    }
    fun getRooms(){
        viewModelScope.launch {
            val result = roomRepository.getRooms()
            when(result){
                is Result.Success -> _rooms.value = result.data
                is Result.Error -> {}
            }
        }
    }
}