package com.example.randomuserclient.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.randomuserclient.R
import com.example.randomuserclient.data.Entitys.User
import com.example.randomuserclient.data.Entitys.UserResponse
import com.example.randomuserclient.ui.theme.RandomUserClientTheme
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

@ExperimentalMaterial3Api
@Composable
fun MainScreen() {

    val viewModel: MainViewModel = hiltViewModel()

    val users by viewModel.users

    val errorMessage by viewModel.errorMessage
    val isLoading by viewModel.isLoading

    val isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    VersionMenu(stringResource(R.string.app_version))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        content = { paddingValues ->
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                state = state,
                indicator = {
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = isRefreshing,
                        containerColor = MaterialTheme.colorScheme.primary,
                        color = MaterialTheme.colorScheme.onSurface,
                        state = state
                    )
                },
                onRefresh = {
                    viewModel.loadUsers()
                }
            ) {
                when {
                    isLoading -> {
                        LoadingIndicator(paddingValues)
                    }
                    errorMessage != null -> {
                        ErrorIndicator(
                            errorMessage = "$errorMessage",
                            paddingValues = paddingValues
                        ) {
                            viewModel.loadUsers()
                        }
                    }
                    else -> { users?.let {
                            UsersContent(
                                usersList = it,
                                paddingValues = paddingValues
                            )
                        }
                    }
                }
            }
        }
    )
}

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
                    maxLines = 2
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
