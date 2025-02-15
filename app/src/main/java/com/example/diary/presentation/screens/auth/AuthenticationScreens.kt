package com.example.diary.presentation.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.diary.util.Constants.CLIENT_ID
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    oneTapState: OneTapSignInState,
    onButtonClicked: () -> Unit

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
                },
                onDialogDismissed = { message ->
                    Log.d("auth", message)
                }
            )
        }
    )
}