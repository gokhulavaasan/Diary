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
        onError: (Exception) -> Unit
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
                /*val result = withContext(Dispatchers.IO) {
                    auth.signInWithCredential(firebaseCredential)
                }
                withContext(Dispatchers.Main) {
                    if (result.isSuccessful) {
                        Log.d("user", "Login Successful")
                        authenticated.value = true
                        Log.d("user", "User Info: ${result.result.user}")
                        Log.d("user", "User Info: ${auth.currentUser?.displayName ?: null}")
                        Log.d("email", "Email: ${auth.currentUser?.email ?: null}")
                        Log.d("displayName", "Name: ${auth.currentUser?.providerData ?: null}")
                        onSuccess()
                    }
                }*/
            } catch (e: Exception) {
                Log.d("user", "Login failure")
                Log.d("error", e.toString())
                onError(e)
            }
        }

        /*.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success")
                val user = auth.currentUser
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                updateUI(null)
            }
        }*/
    }
}