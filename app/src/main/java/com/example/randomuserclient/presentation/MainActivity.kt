package com.example.randomuserclient.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.randomuserclient.presentation.theme.RandomUserClientTheme
import com.example.randomuserclient.presentation.ui.MainScreen
import dagger.hilt.android.AndroidEntryPoint



@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomUserClientTheme {
                MainScreen()
            }
        }
    }
}