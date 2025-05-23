package com.example.diary.presentation.home.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    confirmButton: () -> Unit,
    dialogOpened: Boolean,
    onDismissedButton: () -> Unit,
) {
    if (dialogOpened) {
        AlertDialog(
            confirmButton = {
                Button(onClick = { confirmButton() }) {
                    Text("yes")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { onDismissedButton() }) {
                    Text("No")
                }
            },
            onDismissRequest = {
                onDismissedButton()
            },
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                )
            },
            text = {
                Text(
                    text = message,
                    fontWeight = FontWeight.Normal,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
            }
        )
    }

}