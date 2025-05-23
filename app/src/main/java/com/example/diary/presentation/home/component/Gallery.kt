package com.example.diary.presentation.home.component


import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.diary.R
import kotlin.math.max


@Composable
fun Gallery(
    modifier: Modifier = Modifier,
    images: List<String>,
    imageSize: Dp = 40.dp,
    spaceBetween: Dp = 10.dp,
    imageShape: CornerBasedShape = Shapes().small,
) {
    BoxWithConstraints(modifier = Modifier) {
        val numberOfVisibleImages = remember {
            derivedStateOf {
                max(
                    a = 0,
                    b = this.maxWidth.div(spaceBetween + imageSize).toInt().minus(1)
                )
            }
        }
        val remainingImages = remember {
            derivedStateOf {
                images.size - numberOfVisibleImages.value
            }
        }
        Row {
            images.take(numberOfVisibleImages.value).forEach {
                AsyncImage(
                    modifier = Modifier
                        .clip(imageShape)
                        .size(imageSize),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.placeholder),
                    error =  painterResource(R.drawable.placeholder) ,
                    contentDescription = "Gallery Image"
                )
            }
            Spacer(Modifier.width(spaceBetween))
        }
    }
}