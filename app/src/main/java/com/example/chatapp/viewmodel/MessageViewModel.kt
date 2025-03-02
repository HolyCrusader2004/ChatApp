package com.example.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.Injection
import com.example.chatapp.data.Message
import com.example.chatapp.data.MessageRepository
import com.example.chatapp.data.Result
import com.example.chatapp.data.User
import com.example.chatapp.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MessageViewModel: ViewModel() {
    private val messageRepository: MessageRepository
    private val userRepository: UserRepository

    init {
        messageRepository = MessageRepository(Injection.instance())
        userRepository = UserRepository(FirebaseAuth.getInstance(), Injection.instance())
        loadCurrentUser()
    }

    private val _messages = MutableLiveData<List<Message>>()
    val messages = _messages

    private val _roomid = MutableLiveData<String>()
    private val _currentUser = MutableLiveData<User>()
    val currentUser = _currentUser

    private fun loadCurrentUser(){
        viewModelScope.launch {
            when(val result = userRepository.getCurrentUser()){
                is Result.Success -> _currentUser.value = result.data
                is Result.Error -> {}
            }
        }
    }

    fun loadMessages(){
        viewModelScope.launch {
            if(_roomid.value != null){
                messageRepository.getChatMessages(_roomid.value.toString()).collect{
                    messageList -> _messages.postValue(messageList.map {
                        message -> message.copy(isSentByCurrentUser = message.senderId == (currentUser.value?.email
                    ?: "")
                )
                })
                }
            }
        }
    }

    fun sendMessage(text:String){
        if(_currentUser.value != null){
            val message = Message(
                senderFirstName = _currentUser.value!!.firstName,
                senderId = _currentUser.value!!.email,
                text = text
            )
            Log.d("messagesent", message.toString())
            viewModelScope.launch {
                when(messageRepository.sendMessage(_roomid.value.toString(), message)){
                    is Result.Success -> Unit
                    is Result.Error -> {}
                }
            }
        }
    }

    fun setRoomId(newId: String) {
        _roomid.value = newId

        viewModelScope.launch {
            while (_currentUser.value == null) {
                kotlinx.coroutines.delay(100)
            }
            loadMessages()
        }
    }

}