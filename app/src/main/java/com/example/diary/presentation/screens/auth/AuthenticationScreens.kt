package com.example.diary.presentation.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.diary.util.API_Details.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@Composable
fun AuthenticationScreen(
    authenticated: Boolean,
    loadingState: Boolean,
    oneTapState: OneTapSignInState,
    onButtonClicked: () -> Unit,
    messageBarState: MessageBarState,
    onGetTokenSuccess: (String) -> Unit,
    onGetTokenFailure: (Exception) -> Unit,
    navigateToHome: () -> Unit
) {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        content = {
            ContentWithMessageBar(
                messageBarState = messageBarState
            ) {
                AuthenticationContent(
                    loadingState = loadingState,
                    oneTapState = oneTapState,
                    onButtonClicked = onButtonClicked
                )
            }
        }
    )
    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            Log.d("auth", tokenId)
            onGetTokenSuccess(tokenId)
        },
        onDialogDismissed = { message ->
            Log.d("dialogdismissed", message)

        }
    )
    LaunchedEffect(authenticated) {
        if (authenticated) {
            Log.d("navigate", "navigating to home")
            navigateToHome()
        }
    }
}