package com.example.diary.domain.usecases

import com.example.diary.domain.model.Diary
import com.example.diary.domain.repository.DiaryRepository
import com.example.diary.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiaryUseCases @Inject constructor(
    val diaryRepository: DiaryRepository,
) {
    fun getDiaries(): Flow<Resource<List<Diary>>> {
        return diaryRepository.getDiaries()
    }
}