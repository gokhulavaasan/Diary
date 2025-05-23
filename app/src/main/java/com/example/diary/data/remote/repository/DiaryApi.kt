package com.example.diary.data.remote.repository

import com.example.diary.domain.model.Diary

interface DiaryApi {
    suspend fun getDiaries(): Diary
}