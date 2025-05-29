package com.example.diary.domain.repository

import com.example.diary.domain.model.Diary
import com.example.diary.util.Resource
import kotlinx.coroutines.flow.Flow


interface DiaryRepository {
    suspend fun getDiaries(): Flow<Resource<List<Diary>>>
    suspend fun insertDiary(entry: Diary): Flow<Resource<Diary>>
    suspend fun getDiary(id: String): Flow<Resource<Diary>>
    suspend fun updateDiary(entry: Diary): Flow<Resource<Diary>>
    suspend fun deleteDiary(diaryId: String): Flow<Resource<Boolean>>
}