package com.fedi4.hexolauncher

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp



@Composable
fun PatternPad(
    modifier: Modifier = Modifier,
    state: PatternPadState,
    onPattern: (List<Int>) -> Unit,
    patternRoot: PatternNode.Folder
) {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(
        fontSize = 12.sp,
        color = Color.White,
    )


    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.Black)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { state.onStart(it) },
                    onDrag = { change, _ -> state.onDrag(change.position) },
                    onDragEnd = { state.onEnd(onPattern) }
                )
            }
    ) {

            state.setPoints(
                generatePoints(size, patternRoot, state.pattern)
            )


        // Linien
        for (i in 0 until state.pattern.size - 1) {
            drawLine(
                Color.White,
                state.points[state.pattern[i]].center,
                state.points[state.pattern[i + 1]].center,
                strokeWidth = 6f
            )
        }

        //// Linie zum Finger
        //state.dragPos?.let { pos ->
        //    if (state.pattern.isNotEmpty()) {
        //        drawLine(
        //            Color.White,
        //            state.points[state.pattern.last()].center,
        //            pos,
        //            strokeWidth = 4f
        //        )
        //    }
        //}

        // Punkte
        state.points.forEach { p ->
            if (p.visibility) {

                drawCircle(
                    color = p.color,
                    radius = 24f,
                    center = p.center
                )


                val textToDraw = p.text

                val textLayoutResult = textMeasurer.measure(textToDraw, textStyle)
                drawText(
                    textMeasurer = textMeasurer,
                    text = textToDraw,
                    style = textStyle.copy(color = p.color),
                    topLeft = Offset(
                        x = p.center.x - textLayoutResult.size.width / 2,
                        y = p.center.y + 24f,
                    )
                )
            } else {
                drawCircle(
                    color = Color.Gray,
                    radius = 18f,
                    center = p.center
                )
            }
        }
    }
}
