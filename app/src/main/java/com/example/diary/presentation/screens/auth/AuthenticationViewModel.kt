package com.example.diary.presentation.screens.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


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
        Log.d("sign", "inside sign in")
        auth = Firebase.auth
        viewModelScope.launch {
            try {
                val firebaseCredential = GoogleAuthProvider.getCredential(tokenId, null)
                val result = auth.signInWithCredential(firebaseCredential).await()
                Log.d("user", "Login Successful")
                Log.d("user", result.user?.displayName.toString())
                onSuccess()
            } catch (e: Exception) {
                Log.d("user", "Login failure")
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