package com.example.chatapp.screen

sealed class Screen (val route: String){
    object LoginScreen:Screen("loginscreen")
    object SignUpScreen:Screen("signupscreen")
    object ChatScreen:Screen("chatscreen")
    object ChatRoomsScreen:Screen("chatroomscreen")
}