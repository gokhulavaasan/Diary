package com.example.diary

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.initialize

class Application : Application() {
    override fun onCreate() {
        Firebase.initialize(this)
        super.onCreate()
        /*Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )*/
        Firebase.appCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
    }
}