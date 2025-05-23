package com.example.diary.presentation.home.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.diary.domain.model.Diary
import java.time.LocalDate
import java.time.ZoneId


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    diaries: List<Diary>,
    modifier: Modifier = Modifier,
) {
    if (diaries.isNotEmpty()) {
        val diaryNotes: Map<LocalDate, List<Diary>> = diaries.groupBy {
            it.date.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        }
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            diaryNotes.forEach { (localDate, diaries) ->
                stickyHeader(key = localDate) {
                    DateHeader(localDate = localDate)
                }
                items(
                    items = diaries,
                    key = { it.id }  // Unique key
                ) { diary ->
                    DiaryHolder(diary = diary, onClick = {})
                }
            }
        }
    } else {
        EmptyPage()
    }

}