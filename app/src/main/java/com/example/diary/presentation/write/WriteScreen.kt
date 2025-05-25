package com.example.diary.presentation.write

import android.annotation.SuppressLint
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.diary.domain.model.Diary

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    pagerState: PagerState,
    selectedDiary: Diary?,
    onBackPressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            WriteTopBar(
                selectedDiary = selectedDiary,
                onBackPressed = onBackPressed
            )
        }
    ) {paddingValues ->
        WriteContent(
            paddingValues = paddingValues,
            pagerState = pagerState,
            title = "",
            onTitleChanged ={

            },
            description = "",
            onDescriptionChanged = {}
        )
    }
}