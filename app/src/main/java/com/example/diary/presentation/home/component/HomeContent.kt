package com.example.diary.presentation.home.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.diary.domain.model.Diary
import java.time.LocalDate
import java.time.ZoneId


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    diaries: List<Diary>,
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    navigateToWriteWithArgs: (String) -> Unit,
) {
    if (isLoading == true) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

    } else if (diaries.isNotEmpty()) {
        val diaryNotes: Map<LocalDate, List<Diary>> = diaries.groupBy {
            it.date.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        }
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
                .padding(paddingValues)
        ) {
            diaryNotes.forEach { (localDate, diaries) ->
                stickyHeader(key = localDate) {
                    DateHeader(localDate = localDate)
                }
                items(
                    items = diaries,
                    key = { it.id }  // Unique key
                ) { diary ->
                    DiaryHolder(diary = diary, onClick = navigateToWriteWithArgs)
                }
            }
        }
    } else {
        EmptyPage()
    }

}