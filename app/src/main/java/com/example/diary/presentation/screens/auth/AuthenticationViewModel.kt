package com.example.diary.presentation.screens.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class AuthenticationViewModel : ViewModel() {
    var authenticated = mutableStateOf(false)
        private set
    var isLoading = mutableStateOf(false)
        private set
    private lateinit var auth: FirebaseAuth
    fun setLoading(loading: Boolean) {
        isLoading.value = loading
    }

    fun firebaseUserSignIn(
        tokenId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,
    ) {
        auth = Firebase.auth
        auth.firebaseAuthSettings.forceRecaptchaFlowForTesting(true)
        viewModelScope.launch() {
            try {
                val firebaseCredential = GoogleAuthProvider.getCredential(tokenId, null)
                Log.d("sign", "inside sign in")
                val result = withContext(Dispatchers.IO) {
                    val result = auth.signInWithCredential(firebaseCredential).await()
                    Log.d("user", "Login Successful")
                    authenticated.value = true
                    Log.d("user", "User Info: ${result.additionalUserInfo?.username}")
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.d("user", "Login failure")
                Log.d("error", e.toString())
                onError(e)
            }
        }
    }
}