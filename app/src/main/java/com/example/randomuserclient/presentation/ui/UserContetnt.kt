package com.example.randomuserclient.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.randomuserclient.data.Entitys.User
import com.example.randomuserclient.data.Entitys.UserResponse

@ExperimentalMaterial3Api
@Composable
fun UsersContent(usersList: UserResponse, paddingValues: PaddingValues){
    val sheetState = rememberModalBottomSheetState()
    val lazyGridState = rememberLazyGridState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }

    LazyVerticalGrid (
        columns = GridCells.Fixed(2),
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyGridState,
        modifier = Modifier
            .padding(top = 10.dp)
            .padding(horizontal = 10.dp)

    ){
        items(usersList.results) { user ->
            UserCard(
                user = user,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ){ isShowBottomSheet ->
                showBottomSheet = isShowBottomSheet
                selectedUser = user
            }
        }
    }

    if(showBottomSheet){
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            selectedUser?.let { user ->
                UserDetailCard(user)
            }
        }
    }
}