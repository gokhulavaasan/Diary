package com.example.diary.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DateHeader(
    localDate: Timestamp,
    dateHeaderSize: Int = 180
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = SimpleDateFormat("dd", Locale.ROOT).format(localDate.toDate()),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
            Text(
                text = SimpleDateFormat("EEE", Locale.ENGLISH).format(localDate.toDate()).toUpperCase(),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Light
                ),
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = SimpleDateFormat("MMMM", Locale.ENGLISH).format(localDate.toDate()),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Light
                ),
            )
            Text(
                text = SimpleDateFormat("YYYY", Locale.ENGLISH).format(localDate.toDate()),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.34f),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Light
                ),
            )
        }
    }
}