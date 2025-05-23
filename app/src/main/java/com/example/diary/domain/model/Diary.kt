package com.example.diary.domain.model

import com.google.firebase.Timestamp


data class Diary(
    val id: String,
    val ownerId: String,
    val mood: String,
    val title: String,
    val description: String,
    val images: List<String>,
    val date: Timestamp,
)