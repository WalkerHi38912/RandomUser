package com.example.randomuserclient.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.randomuserclient.R

@Composable
fun VersionMenu(version: String){
    var isExpended by remember { mutableStateOf(false) }
    Button(
        modifier = Modifier.height(50.dp),
        onClick = {
            isExpended = true
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = stringResource(R.string.app_header),
            fontSize = 30.sp,
            fontWeight = FontWeight.Thin
        )
    }

    DropdownMenu(
        expanded = isExpended,
        onDismissRequest = {isExpended = false}
    ) {
        DropdownMenuItem(
            text = {
                Text(version)
            },
            onClick = {}
        )
    }
}

@Composable
fun ErrorIndicator(errorMessage: String, paddingValues: PaddingValues, onRetry: () -> Unit){
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorMessage,
            color = Color.Red,
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            modifier = Modifier.height(50.dp),
            onClick = {
                onRetry()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = stringResource(R.string.retry_api_call),
                fontSize = 30.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
        }
    }
}

@Composable
fun LoadingIndicator(paddingValues: PaddingValues){
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}