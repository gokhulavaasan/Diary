package com.example.diary.domain.repository

import com.example.diary.domain.model.Diary
import com.example.diary.util.Resource
import kotlinx.coroutines.flow.Flow


interface DiaryRepository {
    fun getDiaries(): Flow<Resource<List<Diary>>>
    suspend fun addDiary(entry: Diary): Resource<Unit>
}