package com.fedi4.hexolauncher.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.fedi4.hexolauncher.core.ui.HexoPadViewModel


@Composable
fun PatternTrailCanvas(modifier: Modifier, vm: HexoPadViewModel) {


    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        // handle pointer event
                    }
                }
            },
    ) {


//        for (i in 0..3) {
//            val p = vm.currentPattern[i]
//
//        }

//        drawRect(
//            color = Color.Blue.copy(0.25f),
//        )
    }
}

