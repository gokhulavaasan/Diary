package com.example.diary.presentation.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.diary.util.Constants.CLIENT_ID
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

@Composable
fun AuthenticationScreen(
    authenticated: Boolean,
    loadingState: Boolean,
    oneTapState: OneTapSignInState,
    onButtonClicked: () -> Unit,
    onGetTokenSuccess: (String) -> Unit,
    onGetTokenFailure: (Exception) -> Unit,
    navigateToHome: () -> Unit
) {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    Scaffold(
        content = {
            AuthenticationContent(
                loadingState = loadingState,
                oneTapState = oneTapState,
                onButtonClicked = onButtonClicked
            )

            OneTapSignInWithGoogle(
                state = oneTapState,
                clientId = CLIENT_ID,
                onTokenIdReceived = { tokenId ->
                    Log.d("auth", tokenId)
                    runBlocking {
                        try {
                            val auth = Firebase.auth
                            val firebaseCredential = GoogleAuthProvider.getCredential(tokenId, null)
                            val result = auth.signInWithCredential(firebaseCredential).await()
                            Log.d("user", "Login Successful")
                            Log.d("user", result.user?.displayName.toString())
//                            onSuccess()
                        } catch (e: Exception) {
                            Log.d("user", "Login failure")
//                            onError(e)
                        }
                    }
//                    onGetTokenSuccess(tokenId)
                },
                onDialogDismissed = { message ->
                    Log.d("dialogdismissed", message)

                }
            )
        }
    )
    LaunchedEffect(authenticated) {
        if (authenticated) {
            navigateToHome()
        }
    }
}