package com.example.diary.presentation.write

import android.annotation.SuppressLint
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.diary.domain.model.Diary
import com.example.diary.domain.model.Mood
import com.google.firebase.Timestamp
import kotlinx.coroutines.coroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    pagerState: PagerState,
    onBackPressed: () -> Unit,
    uiState: UiState,
    moodName: () -> String,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDateTimeUpdated: (Timestamp) -> Unit,
    onSaveClicked: (Diary) -> Unit,
    onDeleteClicked: () -> Unit,
) {

    LaunchedEffect(uiState.mood) {
        coroutineScope {
            pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
        }
    }
    Scaffold(
        topBar = {
            WriteTopBar(
                moodName = moodName,
                selectedDiary = uiState.selectedDiary,
                onBackPressed = onBackPressed,
                onDateTimeUpdated = onDateTimeUpdated,
                onDeleteClicked = onDeleteClicked
            )
        }
    ) { paddingValues ->
        WriteContent(
            uiState = uiState,
            paddingValues = paddingValues,
            pagerState = pagerState,
            title = uiState.title,
            onTitleChanged = onTitleChanged,
            description = uiState.description,
            onDescriptionChanged = onDescriptionChanged,
            onSaveClicked = onSaveClicked
        )
    }
}