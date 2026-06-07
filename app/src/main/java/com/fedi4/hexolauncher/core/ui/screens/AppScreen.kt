package com.fedi4.hexolauncher.core.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun AppScreen(modifier: Modifier = Modifier) {

    Box(modifier = modifier.fillMaxSize().background(Color(0x66224422))) {
        Text("App Screen", Modifier.align(Alignment.Center))
    }
}