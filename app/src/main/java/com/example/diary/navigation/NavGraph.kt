package com.example.diary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diary.presentation.screens.auth.AuthenticationScreen
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
        val oneTapState = rememberOneTapSignInState()
        AuthenticationScreen(
            loadingState = oneTapState.opened,
            onButtonClicked = {
                oneTapState.open()
            },
            oneTapState = oneTapState
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