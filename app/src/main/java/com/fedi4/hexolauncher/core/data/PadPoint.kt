package com.fedi4.hexolauncher.core.data

import androidx.compose.ui.graphics.Color

data class PadPoint(
    val index: Int,

    val text: String,
    val color: Color = Color.White,
    val visibility: Boolean = true,

    val icon: String? = null,
    val radius: Float = 24f,

    val type: PadPointType? = null
)
enum class PadPointType(){
    FOLDER, ACTION, APP, EMPTY
}