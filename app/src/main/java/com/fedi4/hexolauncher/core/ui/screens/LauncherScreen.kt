package com.fedi4.hexolauncher.core.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fedi4.hexolauncher.core.ui.components.patternpad.PatternPadWrapper
import com.fedi4.hexolauncher.core.ui.components.TimeWidget

@Composable
fun LauncherScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
            .padding(bottom = Dp(50f))
            .padding(16.dp)
            .background(color = androidx.compose.ui.graphics.Color.Transparent)
    ) {
        TimeWidget(modifier = Modifier.weight(0.7f))
        Spacer(modifier = Modifier.weight(0.7f))
        PatternPadWrapper(
            modifier = Modifier.weight(1.1f).fillMaxWidth().align(Alignment.CenterHorizontally)
        )
    }
}



@Preview()
@Composable
fun LauncherScreenPreview() {
    LauncherScreen(modifier = Modifier.fillMaxSize())
}

