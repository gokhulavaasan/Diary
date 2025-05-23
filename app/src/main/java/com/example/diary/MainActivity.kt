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
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.diary.navigation.Screen
import com.example.diary.navigation.SetUpNavGraph
import com.example.diary.ui.theme.DiaryTheme
import com.example.diary.util.Constants.auth
import com.example.diary.util.Constants.user
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var keepSplashOpened = true
//    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            keepSplashOpened
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
//        enableEdgeToEdge()
        auth = Firebase.auth
        setContent {
            DiaryTheme {
                user = auth.currentUser
                val navController = rememberNavController()
                SetUpNavGraph(
                    context = this,
                    startDestination = getStartRoute(),
                    navController = navController,
                    onDataLoaded = {
                        keepSplashOpened = false
                    }
                )
            }
        }
    }
}


fun getStartRoute(): String {
    if (user == null) {
        Log.d("usersignstatus", "user is not signed")
        return Screen.Authentication.route
    }
    Log.d("username", user?.displayName.toString())
    return Screen.Home.route
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