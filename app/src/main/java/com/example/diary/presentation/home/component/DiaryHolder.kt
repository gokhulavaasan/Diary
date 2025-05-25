package com.example.diary.presentation.home.component

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diary.domain.model.Diary
import com.example.diary.domain.model.Mood
import com.example.diary.domain.model.getDate
import com.example.diary.ui.theme.Elevation.Level1
import com.google.firebase.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("NewApi")
@Composable
fun DiaryHolder(
    diary: Diary,
    onClick: (String) -> Unit,
) {
    val localDensity = LocalDensity.current
    var componentState by remember { mutableStateOf(0.dp) }
    var galleryOpened by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            ) {
                onClick(diary.id)
            }
    ) {
        Spacer(Modifier.width(5.dp))
        Surface(
            Modifier
                .width(2.dp)
                .height(componentState + 14.dp),
            tonalElevation = Level1
        ) {}
        Spacer(Modifier.width(10.dp))
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .onGloballyPositioned {
                    componentState = with(localDensity) { it.size.height.toDp() }
                }
        ) {
            Column {
                val mood by remember { mutableStateOf(Mood.valueOf(diary.mood)) }
                val formatter = remember {
                    DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
                        .withZone(ZoneId.systemDefault())
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(mood.containerColor)
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(id = mood.icon),
                            contentDescription = "Current Mood Icon"
                        )
                        Spacer(Modifier.width(7.dp))
                        Text(
                            text = mood.name,
                            color = mood.contentColor
                        )
                    }
                    Text(
//                        text = diary.getDate(diary),
                        text = formatter.format(diary.getDate(diary)),
                        color = mood.contentColor
                    )
                }
                Text(
                    modifier = Modifier.padding(14.dp),
                    text = diary.description,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                )
                if (diary.images.isNotEmpty()) {
                    TextButton(onClick = {
                        galleryOpened = !galleryOpened
                    }) {
                        Text(
                            text = if (galleryOpened) "Close Gallery" else "Show Gallery",
                            style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize)
                        )
                    }
                }
                AnimatedVisibility(
                    visible = galleryOpened,
                    enter = fadeIn() + expandVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Gallery(
                            images = diary.images,
                        )
                    }

                }
            }
        }
    }
}


@Composable
@Preview
fun DiaryHolderPreview() {
    DiaryHolder(
        diary = Diary(
            id = "1234",
            ownerId = "1234",
            mood = Mood.Happy.name,
            title = "LOREM IPSUM",
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            images = listOf("", ""),
            date = Timestamp.now()
        ),
        onClick = {}
    )
}                                                                                                                                                                                                                                                    
