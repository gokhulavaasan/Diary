package com.example.diary.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diary.domain.usecases.DiaryUseCases

class HomeViewModelFactory(
    private val diaryUseCases: DiaryUseCases,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(diaryUseCases) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}