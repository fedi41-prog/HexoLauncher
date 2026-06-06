package com.fedi4.hexolauncher.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.fedi4.hexolauncher.core.ui.HexoPadViewModel
import com.fedi4.hexolauncher.core.util.hexoPosition


@Composable
fun PatternTrailCanvas(modifier: Modifier, vm: HexoPadViewModel, patternPadGeometry: PatternPadGeometry) {

    val center = Offset(patternPadGeometry.layoutSize / 2, patternPadGeometry.layoutSize / 2)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        if (vm.isDragging.value) {

            for (a in 0..3) {
                val i = vm.currentPattern.size - a - 1
                if (i < 0) break
                val point = vm.currentPattern[i]

                var nextPos = vm.pointerPosition.value
                if (i < vm.currentPattern.size - 1) {
                    nextPos = hexoPosition( vm.currentPattern[i+1], center, patternPadGeometry.radius)
                }

                val pos = hexoPosition(point, center, patternPadGeometry.radius)

                drawLine(
                    color = Color.White.copy(1f/(a+2)),
                    start = pos,
                    end = nextPos,
                    strokeWidth = 200f,
                    cap = StrokeCap.Round
                )


            }
        }
    }
}
