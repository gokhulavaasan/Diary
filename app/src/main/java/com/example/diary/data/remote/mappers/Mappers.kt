package com.example.diary.data.remote.mappers

import com.example.diary.data.remote.repository.DiaryDto
import com.example.diary.domain.model.Diary


fun DiaryDto.toDiary(id: String): Diary {
    return Diary(
        id = id,
        ownerId = ownerId,
        mood = mood,
        title = title,
        description = description,
        images = images,
        date = date
    )
}