package com.example.diary

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.diary.navigation.Screen
import com.example.diary.navigation.SetUpNavGraph
import com.example.diary.ui.theme.DiaryTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private var keepSplashOpened = true
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        installSplashScreen()
//        enableEdgeToEdge()
        auth = Firebase.auth
        setContent {
            DiaryTheme {

                val user = auth.currentUser
                val navController = rememberNavController()
                if (user == null) {
                    Log.d("user", "user is not signed")
                    SetUpNavGraph(
                        startDestination = Screen.Authentication.route,
                        navController = navController
                    )
                }
                Log.d("user", user?.displayName.toString())
                /*Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }*/
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiaryTheme {
        Greeting("Android")
    }
}