package com.example.randomuserclient.presentation.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.randomuserclient.R
import com.example.randomuserclient.data.Entitys.User

@ExperimentalMaterial3Api
@Composable
fun UserCard(
    user: User,
    showName: Boolean = true,
    modifier: Modifier,
    showBottomSheet: (Boolean) -> Unit,
) {
    val userName = user.name.first + " " + user.name.last
    val isNameLong = userName.length > 20
    val userPhoto = user.picture.large

    val imageSize = if (isNameLong) 128.dp else 160.dp
    val fontSize = if (isNameLong) 16.sp else 20.sp

    Card(
        modifier = modifier
            .clickable {
                showBottomSheet(true)
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Unspecified)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(imageSize)
                    .clip(RoundedCornerShape(64.dp))
            ) {
                AsyncImage(
                    model = userPhoto,
                    contentScale = ContentScale.Fit,
                    contentDescription = stringResource(R.string.users_photo_description),
                    modifier = Modifier.fillMaxSize()
                )
            }
            if(showName){
                Spacer(Modifier.height(16.dp))
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = fontSize),
                    color = Color.White,
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun UserDetailCard(user: User){
    val context = LocalContext.current

    val userName = user.name.first + " " + user.name.last + "," + " ${user.dob.age}"
    val userLocation = user.location.city + "," + " " + user.location.country
    val userPhoto = user.picture.large
    val userEmail = user.email
    val userPhone = user.phone

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp, start = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(64.dp))
        ) {
            AsyncImage(
                model = userPhoto,
                contentScale = ContentScale.Fit,
                contentDescription = stringResource(R.string.users_photo_description),
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = userLocation,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.clickable {
                val uri = "geo:0,0?q=${userLocation}".toUri()
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }
        )
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 32.dp)
        ){
            Text(
                text = stringResource(R.string.email),
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = userEmail,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:${userEmail}".toUri()
                    }
                    context.startActivity(intent)
                }
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.call),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = userPhone,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = "tel:${userPhone}".toUri()
                    }
                    context.startActivity(intent)
                }
            )
        }
    }
}
