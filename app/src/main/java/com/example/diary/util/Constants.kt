package com.example.diary.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object Constants {
    lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null
    const val diaryId = "diaryId"
}