package com.example.diary.domain.model

import android.annotation.SuppressLint
import java.time.Instant

@SuppressLint("NewApi")
fun Diary.getDate(diary: Diary): Instant {
    val date = diary.date.toDate()
    val time = date.time
    val sec: Long = diary.date.seconds
    val nano: Int = diary.date.nanoseconds
    return if (sec >= 0) {
        Instant.ofEpochSecond(sec, nano.toLong())
    } else {
        Instant.ofEpochSecond(sec - 1, 1_000_000 + nano.toLong())
    }
}