package com.fedi4.hexolauncher.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.fedi4.hexolauncher.core.data.PadPoint

data class PatternPadUiState (
    var geometry: MutableState<PatternPadGeometry> = mutableStateOf(PatternPadGeometry(0f, 0f, 0f, 0f)),

    val points: MutableList<PadPoint> = mutableStateListOf<PadPoint>(),
    val currentPattern: MutableList<Int> = mutableStateListOf<Int>(),

    val isDragging: MutableState<Boolean> = mutableStateOf(false),
    val pointerPosition: MutableState<Offset> = mutableStateOf(Offset.Companion.Zero),
    )
data class PatternPadGeometry(
    val radius: Float,
    val itemSize: Float,
    val iconSize: Float,
    val layoutSize: Float
) {
    val layoutSizeDp: Dp
        @Composable
        get() = with(LocalDensity.current) { layoutSize.toDp() }
    val radiusDp: Dp
        @Composable
        get() = with(LocalDensity.current) { radius.toDp() }
    val itemSizeDp: Dp
        @Composable
        get() = with(LocalDensity.current) { itemSize.toDp() }
    val iconSizeDp: Dp
        @Composable
        get() = with(LocalDensity.current) { iconSize.toDp() }

    companion object {
        fun fromLayoutSize(layoutSize: Float): PatternPadGeometry {

            val itemSize = (layoutSize * 0.32f)

            return PatternPadGeometry(
                itemSize = itemSize,
                iconSize = (itemSize * 0.8f),
                radius = ((layoutSize-itemSize) / 2),
                layoutSize = layoutSize
            )
        }
    }
}