package com.example.chatapp.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatapp.viewmodel.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navController: NavController,
    viewModel: AuthViewModel
) {
    NavHost(navController = navController as NavHostController,
        startDestination = Screen.SignUpScreen.route){
        composable(Screen.SignUpScreen.route){
            SignUpScreen (
                onNavigateToLogin = {
                    navController.navigate(Screen.LoginScreen.route)
                },
                viewModel = viewModel
            )
        }
        composable(Screen.LoginScreen.route){
            SignInScreen (viewModel = viewModel,
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUpScreen.route)
                },
                onSignIn = {
                    navController.navigate(Screen.ChatRoomsScreen.route)
                }
            )
        }
        composable(Screen.ChatRoomsScreen.route){
            ChatRoomListScreen{
                navController.navigate("${Screen.ChatScreen.route}/${it.id}")
            }
        }
        composable("${Screen.ChatScreen.route}/{roomId}"){
            val roomId: String = it.arguments?.getString("roomId") ?: ""
            ChatScreen(roomId = roomId)
        }
    }
}