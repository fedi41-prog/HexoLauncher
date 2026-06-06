package com.fedi4.hexolauncher.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PatternTrailCanvas(modifier: Modifier) {


    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        drawRect(
            color = Color.Blue.copy(0.25f),
        )
    }
}
