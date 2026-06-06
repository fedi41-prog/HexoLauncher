package com.fedi4.hexolauncher.core.ui.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints

@Composable
fun BackgroundMatch(
    content: @Composable () -> Unit,
    bg: @Composable () -> Unit
) {
    /// Second gets placed under content
    SubcomposeLayout { constraints ->

        val main = subcompose("first", content)
            .map { it.measure(constraints) }

        val maxWidth = main.maxOf { it.width }
        val maxHeight = main.maxOf { it.height }

        val secondPlaceables = subcompose("second", bg)
            .map {
                it.measure(
                    Constraints.fixed(maxWidth, maxHeight)
                )
            }

        layout(maxWidth, maxHeight) {
            secondPlaceables.forEach { it.place(0, 0) }
            main.forEach { it.place(0, 0) }
        }
    }
}