package com.example.chatapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.data.Room
import com.example.chatapp.viewmodel.RoomViewModel

@Composable
fun ChatRoomListScreen(
    roomViewModel: RoomViewModel = viewModel(),
    onRoomJoined: (Room) -> Unit
){
    var showDialog by remember {
        mutableStateOf(false)
    }
    var name by remember {
        mutableStateOf("")
    }

    val rooms by roomViewModel.rooms.observeAsState(initial = emptyList())

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Chat Rooms", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn{
            items(rooms){
                room -> RoomItem(room = room, onRoomClicked = { onRoomJoined(room) })
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            showDialog = true
        },
            modifier = Modifier.fillMaxWidth()) {
            Text(text = "Create Room")
        }

        if (showDialog){
            AlertDialog( onDismissRequest = { showDialog = true },
                title = { Text("Create a new room") },
                text={
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }, confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                if (name.isNotBlank()) {
                                    roomViewModel.createRoom(name)
                                    showDialog = false
                                    roomViewModel.getRooms()
                                }
                            }
                        ) {
                            Text("Add")
                        }
                        Button(
                            onClick = { showDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                })
        }

    }
}

@Composable
fun RoomItem(room: Room, onRoomClicked: (Room) -> Unit){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = room.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        OutlinedButton(onClick = { onRoomClicked(room) }) {
            Text(text = "Join")
        }
    }
}