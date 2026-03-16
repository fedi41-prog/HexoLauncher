package com.fedi4.hexolauncher.appsorter

import androidx.compose.ui.graphics.ImageBitmap

data class AppInfo (
    val icon: ImageBitmap,
    val name: String,
    val packageName: String
)