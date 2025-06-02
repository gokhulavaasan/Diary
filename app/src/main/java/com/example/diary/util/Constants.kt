package com.example.diary.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object Constants {
    lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null
    const val diaryId = "diaryId"

    const val IMAGES_DATABASE = "images_db"
    const val IMAGE_TO_UPLOAD_TABLE = "image_to_upload_table"
    const val IMAGE_TO_DELETE_TABLE = "image_to_delete_table"
}