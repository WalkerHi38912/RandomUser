package com.example.randomuserclient.presentation.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.randomuserclient.R
import com.example.randomuserclient.presentation.MainViewModel

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