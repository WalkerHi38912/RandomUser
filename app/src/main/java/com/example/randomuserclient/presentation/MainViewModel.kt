package com.example.randomuserclient.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuserclient.data.Entitys.UserResponse
import com.example.randomuserclient.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel()
{
    var users = mutableStateOf<UserResponse?>(null)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    var isLoading = mutableStateOf(false)
        private set

    init {
        loadUsers()
    }

    fun loadUsers(){
        isLoading.value = true
        viewModelScope.launch {
            try {
                users.value = api.getUsersList(20)
                errorMessage.value = null
            } catch (e: Exception){
                errorMessage.value = "Check Network Connection!"
            } finally {
                isLoading.value = false
            }
        }
    }
}
