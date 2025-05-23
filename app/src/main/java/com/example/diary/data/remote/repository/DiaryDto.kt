package com.example.diary.data.remote.repository

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class DiaryDto(
    val ownerId: String = "",
    val mood: String = "",
    val title: String = "",
    val description: String = "",
    val images: List<String> = emptyList(),
    val date: Timestamp = Timestamp.now(),
)