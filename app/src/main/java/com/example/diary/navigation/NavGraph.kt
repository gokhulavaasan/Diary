package com.example.diary.navigation

import android.util.Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diary.presentation.home.HomeScreen
import com.example.diary.presentation.home.component.DisplayAlertDialog
import com.example.diary.presentation.screens.auth.AuthenticationScreen
import com.example.diary.presentation.screens.auth.AuthenticationViewModel
import com.example.diary.util.Constants.auth
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetUpNavGraph(
    startDestination: String,
    navController: NavHostController,
    onDataLoaded: () -> Unit,
) {
    NavHost(
        startDestination = startDestination,
        navController = navController
    ) {
        authenticationRoute(
            onDataLoaded,
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            }
        )
        homeRoute(
            onDataLoaded,
            navController = navController
        )
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(
    onDataLoaded: () -> Unit,
    navigateToHome: () -> Unit
) {
    composable(route = Screen.Authentication.route) {
        val authViewModel: AuthenticationViewModel = viewModel()
        val authenticated by authViewModel.authenticated
        val isLoading by authViewModel.isLoading
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }
        AuthenticationScreen(
            authenticated = authenticated,
            loadingState = isLoading,
            onButtonClicked = {
                oneTapState.open()
                authViewModel.setLoading(true)
            },
            oneTapState = oneTapState,
            messageBarState = messageBarState,
            onGetTokenSuccess = { tokenId ->
                authViewModel.setLoading(true)
                authViewModel.firebaseUserSignIn(
                    tokenId = tokenId,
                    onSuccess = {
                        messageBarState.addSuccess("LOGIN SUCCESSFUL")
                        authViewModel.setLoading(false)
                    },
                    onError = {
                        messageBarState.addError(it)
                        authViewModel.setLoading(false)
                    }
                )
            },
            onGetTokenFailure = { exception ->
                messageBarState.addError(Exception(exception))
                authViewModel.setLoading(false)
                Log.d("exception ->", exception.toString())
            },
            navigateToHome = navigateToHome,
        )
    }
}

fun NavGraphBuilder.homeRoute(
    onDataLoaded: () -> Unit,
    navController: NavController
) {
    composable(route = Screen.Home.route) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var signOutDialogOpened by remember { mutableStateOf(false) }
        var deleteAllDialogOpened by remember { mutableStateOf(false) }
        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }
        HomeScreen(
            navigateToWrite = {
                navController.popBackStack()
                navController.navigate(Screen.Write.route)
            },
            drawerState = drawerState,
            onSignOutClicked = {
                signOutDialogOpened = true
            },
            onDeleteAllClicked = {
                deleteAllDialogOpened = true
            },
            menuClicked = {
                scope.launch {
                    drawerState.open()
                }
            }
        )
        DisplayAlertDialog(
            title = "SIGN OUT",
            message = "Are you sure you want to Sign Out from your Google Account?",
            dialogOpened = signOutDialogOpened,
            confirmButton = {
                scope.launch(Dispatchers.IO) {
                    auth.signOut()
                    signOutDialogOpened = false
                    withContext(Dispatchers.Main) {
                        navController.popBackStack()
                        navController.navigate(Screen.Authentication.route)
                    }
                }
            },
            onDismissedButton = {
                Log.d("dialogclosed", "dialog close called")
                signOutDialogOpened = false

            },
        )
    }
}

fun NavGraphBuilder.writeRoute() {
    composable(route = Screen.Write.route) {

    }
}