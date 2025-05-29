package com.example.diary.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.diary.domain.model.Diary
import com.example.diary.domain.model.Mood
import com.example.diary.presentation.home.HomeScreen
import com.example.diary.presentation.home.HomeViewModel
import com.example.diary.presentation.home.component.DisplayAlertDialog
import com.example.diary.presentation.screens.auth.AuthenticationScreen
import com.example.diary.presentation.screens.auth.AuthenticationViewModel
import com.example.diary.presentation.write.WriteScreen
import com.example.diary.presentation.write.WriteViewModel
import com.example.diary.util.Constants.auth
import com.example.diary.util.Constants.diaryId
import com.google.firebase.Timestamp
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetUpNavGraph(
    context: ViewModelStoreOwner,
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
            context,
            onDataLoaded,
            navController = navController,
            navigateToWrite = {
                navController.navigate(Screen.Write.route)
            },
            navigateToWriteWithArgs = {
                navController.navigate(Screen.Write.passDiaryId(it))
            },
        )
        writeRoute(onBackPressed = {
            navController.popBackStack()
        })
    }
}

fun NavGraphBuilder.authenticationRoute(
    onDataLoaded: () -> Unit,
    navigateToHome: () -> Unit,
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
    context: ViewModelStoreOwner,
    onDataLoaded: () -> Unit,
    navController: NavController,
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
) {
    composable(route = Screen.Home.route) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var signOutDialogOpened by remember { mutableStateOf(false) }
        var deleteAllDialogOpened by remember { mutableStateOf(false) }
        val homeViewModel: HomeViewModel = hiltViewModel()
        val diaries by homeViewModel.diaries.collectAsState()
        val isLoading by homeViewModel.isLoading.collectAsState()
        LaunchedEffect(key1 = isLoading) {
            if (isLoading != true) {
                onDataLoaded()
            }

        }
        HomeScreen(
            diaries = diaries,
            navigateToWrite = navigateToWrite,
            navigateToWriteWithArgs = navigateToWriteWithArgs,
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
            },
            isLoading = isLoading
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

fun NavGraphBuilder.writeRoute(onBackPressed: () -> Unit) {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(name = diaryId) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) { navBackStackEntry ->
        val viewModel: WriteViewModel = hiltViewModel(navBackStackEntry)
        val context = LocalContext.current
        val uiState = viewModel.uiState
        val pagerState = rememberPagerState(
            pageCount = { Mood.entries.size }
        )
        WriteScreen(
            uiState = uiState,
            pagerState = pagerState,
            onBackPressed = onBackPressed,
            onTitleChanged = { viewModel.setTitle(title = it) },
            onDescriptionChanged = { viewModel.setDescription(description = it) },
            onDateTimeUpdated = { viewModel.updateDateTime(timeStamp = it) },
            moodName = { Mood.entries[pagerState.currentPage].name },
            onSaveClicked = {
                viewModel.upsertDiary(
                    diary = it,
                    onSuccess = {
                        onBackPressed()
                    },
                    onError = { message ->
                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            },
            onDeleteClicked = {
                viewModel.deleteDiary(
                    onSuccess = {
                        Toast.makeText(context, "Diary deleted", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    },
                    onError = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
    }
}

val diary = Diary(
    id = "1234",
    ownerId = "1234",
    mood = Mood.Happy.name,
    title = "LOREM IPSUM",
    description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
    images = listOf("", ""),
    date = Timestamp.now()
)