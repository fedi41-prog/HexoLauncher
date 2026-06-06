package com.fedi4.hexolauncher.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class PatternPadGeometry (
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