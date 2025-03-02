package com.example.chatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.Injection
import com.example.chatapp.data.Result
import com.example.chatapp.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val userRepository: UserRepository

    init {
        userRepository = UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )
    }

    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult = _authResult

    fun signUp(email:String, password:String, firstName: String, lastName:String){
        viewModelScope.launch {
            _authResult.value = userRepository.signUp(email = email, firstName = firstName, lastName = lastName, password = password)
        }
    }

    fun login(email: String, password: String){
        viewModelScope.launch {
            _authResult.value = userRepository.login(email = email, password = password)
        }
    }
}