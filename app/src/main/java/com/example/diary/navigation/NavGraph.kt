package com.example.diary.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diary.presentation.screens.auth.AuthenticationScreen
import com.example.diary.presentation.screens.auth.AuthenticationViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState

@Composable
fun SetUpNavGraph(
    startDestination: String,
    navController: NavHostController
) {
    NavHost(
        startDestination = startDestination,
        navController = navController
    ) {
        authenticationRoute()
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute() {
    composable(route = Screen.Authentication.route) {
        val authViewModel: AuthenticationViewModel = viewModel()
        val authenticated = authViewModel.authenticated
        val isLoading = authViewModel.isLoading
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        AuthenticationScreen(
            authenticated = authViewModel.authenticated.value,
            loadingState = authViewModel.isLoading.value,
            onButtonClicked = {
                oneTapState.open()
            },
            oneTapState = oneTapState,
            onGetTokenSuccess = { tokenId ->
                authViewModel.setLoading(true)
                authViewModel.firebaseUserSignIn(
                    tokenId = tokenId,
                    onSuccess = {
                        authViewModel.setLoading(false)
                    },
                    onError = {
                        authViewModel.setLoading(false)
                    }
                )
            },
            onGetTokenFailure = { exception ->
                authViewModel.setLoading(false)
                Log.d("exception ->", exception.toString())
            },
            navigateToHome = {},
        )
    }
}

fun NavGraphBuilder.homeRoute() {
    composable(route = Screen.Home.route) {

    }
}

fun NavGraphBuilder.writeRoute() {
    composable(route = Screen.Write.route) {

    }
}