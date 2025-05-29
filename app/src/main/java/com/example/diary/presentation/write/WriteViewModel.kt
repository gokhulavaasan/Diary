package com.example.diary.presentation.write

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.model.Diary
import com.example.diary.domain.model.Mood
import com.example.diary.domain.usecases.DiaryUseCases
import com.example.diary.util.Constants.diaryId
import com.example.diary.util.Resource
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val diaryUseCases: DiaryUseCases,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getDiaryArgument()
        fetchSelectedDiary()
    }

    private fun fetchSelectedDiary() {
        if (uiState.selectedDiaryId != null) {
            viewModelScope.launch {
                diaryUseCases
                    .getDiary(uiState.selectedDiaryId.toString())
                    .collect() { result ->
                        when (result) {
                            is Resource.Error -> {}
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                val diary = result.data
                                if (diary != null) {
                                    uiState = uiState.copy(
                                        selectedDiary = diary,
                                        title = diary.title,
                                        description = diary.description,
                                        mood = Mood.valueOf(diary.mood),
                                    )
                                }
                            }
                        }
                    }
            }
        }

    }

    private fun getDiaryArgument() {
        uiState = uiState.copy(
            selectedDiaryId = savedStateHandle.get<String>(
                key = diaryId
            )
        )
    }

    private fun setSelectedDiary(diary: Diary) {
        uiState = uiState.copy(selectedDiary = diary)
    }

    fun setTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun setDescription(description: String) {
        uiState = uiState.copy(description = description)
    }

    private fun setMood(mood: Mood) {
        uiState = uiState.copy(mood = mood)
    }

    fun updateDateTime(timeStamp: Timestamp) {
        uiState = uiState.copy(updatedDateTime = timeStamp)
    }

    fun upsertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            if (uiState.selectedDiaryId != null) {
                updateDiary(diary, onSuccess, onError)
            } else {
                insertDiary(diary, onSuccess, onError)
            }
        }
    }

    fun deleteDiary(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            diaryUseCases.deleteDiary(uiState.selectedDiaryId.toString()).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        withContext(Dispatchers.Main) {
                            onError(result.message.toString())
                        }
                    }

                    is Resource.Loading<*> -> {}
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            onSuccess()
                        }
                    }
                }
            }

        }
    }

    suspend fun insertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        diaryUseCases.insertDiary(diary).collect { result ->
            when (result) {
                is Resource.Error -> {
                    withContext(Dispatchers.Main) {
                        onError(result.message.toString())
                    }
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    onSuccess()
                }
            }
        }
    }

    suspend fun updateDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        diaryUseCases.updateDiary(diary).collect { result ->
            when (result) {
                is Resource.Error -> {
                    withContext(Dispatchers.Main) {
                        onError(result.message.toString())
                    }
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    onSuccess()
                }
            }
        }
    }
}

data class UiState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral,
    val updatedDateTime: Timestamp? = null,
)