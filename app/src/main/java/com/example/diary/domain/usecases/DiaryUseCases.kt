package com.example.diary.domain.usecases

import com.example.diary.domain.model.Diary
import com.example.diary.domain.repository.DiaryRepository
import com.example.diary.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiaryUseCases @Inject constructor(
    val diaryRepository: DiaryRepository,
) {
    suspend fun getDiaries(): Flow<Resource<List<Diary>>> {
        return diaryRepository.getDiaries()
    }

    suspend fun getDiary(id: String): Flow<Resource<Diary>> {
        return diaryRepository.getDiary(id)
    }

    suspend fun insertDiary(dairy: Diary): Flow<Resource<Diary>> {
        return diaryRepository.insertDiary(dairy)
    }

    suspend fun updateDiary(dairy: Diary): Flow<Resource<Diary>> {
        return diaryRepository.updateDiary(dairy)
    }

    suspend fun deleteDiary(dairyId: String): Flow<Resource<Boolean>> {
        return diaryRepository.deleteDiary(dairyId)
    }
}