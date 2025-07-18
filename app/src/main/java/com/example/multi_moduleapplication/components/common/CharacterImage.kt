package com.example.multi_moduleapplication.components.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage

val defaultModifier = Modifier
    .fillMaxWidth()
    .aspectRatio(1f)
    .clip(RoundedCornerShape(12.dp))

@Composable
fun CharacterImage(
    imageUrl: String, modifier: Modifier = defaultModifier
) {
    Box {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = "Character Image",
            modifier = modifier,
            loading = { LoadingState() }
        )
    }
}