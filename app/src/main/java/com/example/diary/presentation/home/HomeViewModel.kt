package com.example.diary.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.model.Diary
import com.example.diary.domain.usecases.DiaryUseCases
import com.example.diary.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val diaryUseCases: DiaryUseCases,
) : ViewModel() {
    private val _diaries = MutableStateFlow<List<Diary>>(emptyList())
    val diaries: StateFlow<List<Diary>> get() = _diaries
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        getDiaries()
        _isLoading.value = true
    }

    fun getDiaries() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            diaryUseCases.getDiaries().collect() { result ->
                when (result) {
                    is Resource.Error -> {
                        _isLoading.value = false
                    }

                    is Resource.Loading -> _isLoading.value = true
                    is Resource.Success -> {
                        _diaries.value = result.data ?: emptyList()
                        _isLoading.value = false
                    }
                }
            }
        }

    }
}