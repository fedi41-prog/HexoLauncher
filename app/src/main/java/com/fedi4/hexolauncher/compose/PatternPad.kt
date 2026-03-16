package com.fedi4.hexolauncher.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import com.fedi4.hexolauncher.MainActivity
import com.fedi4.hexolauncher.PatternNode
import com.fedi4.hexolauncher.PatternPadState
import com.fedi4.hexolauncher.generatePoints
import com.fedi4.hexolauncher.getCircleBitmap
import com.fedi4.hexolauncher.loadAppIcon
import kotlin.math.roundToInt


@Composable
fun PatternPad(
    modifier: Modifier = Modifier,
    state: PatternPadState,
    onPattern: (List<Int>) -> Unit,
    patternRoot: PatternNode.Folder
) {

    val icons: MutableMap<String, ImageBitmap> = remember {mutableMapOf()}
    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(
        fontSize = 12.sp,
        color = Color.White,
    )
    val hexagon = remember {
        RoundedPolygon(
            6,
            rounding = CornerRounding(0.2f)
        )
    }
    val clip = remember(hexagon) {
        RoundedPolygonShape(polygon = hexagon)
    }


    fun getIcon(packageName: String): ImageBitmap {
        if (icons.containsKey(packageName)) return icons.getValue(packageName)
        val context = MainActivity.Companion.applicationContext()
        val drawable = loadAppIcon(context.packageManager, packageName)
        if (drawable != null) {
            icons[packageName] = getCircleBitmap(drawable.toBitmap(), 2).asImageBitmap()
            return icons.getValue(packageName)
        }
        return ImageBitmap(1, 1)
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)

        .clip(clip)
    ) {


            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.Black.copy(0.5f))
                    .clip(RoundedPolygonShape(hexagon))
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

                // Linie zum Finger
                state.dragPos?.let { pos ->
                    if (state.pattern.isNotEmpty() && pos.value != null) {
                        drawLine(
                            color = Color.White,
                            start = state.points[state.pattern.last()].center,
                            end = pos.value!!,
                            strokeWidth = 4f
                        )
                    }
                }

                // Punkte
                state.points.forEach { p ->

                    // DRAW POINTS =====================
                    if (p.type == "app") {
                        if (p.icon != null) {
                            drawImage(
                                getIcon(p.icon),
                                dstOffset = (p.center - Offset(p.radius, p.radius)).round(),
                                dstSize = IntSize((p.radius * 2).roundToInt(), (p.radius * 2).roundToInt())
                            )
                        }
                        else {
                            drawCircle(
                                color = p.color,
                                radius = p.radius,
                                center = p.center,
                                style = Stroke(width = 4f)
                            )
                        }

                        val textToDraw = p.text

                        val textLayoutResult = textMeasurer.measure(textToDraw, textStyle)
                        drawText(
                            textMeasurer = textMeasurer,
                            text = textToDraw,
                            style = textStyle.copy(color = p.color),
                            topLeft = Offset(
                                x = p.center.x - textLayoutResult.size.width / 2,
                                y = p.center.y + p.radius,
                            )
                        )
                    } else if (p.type == "folder") {
                        drawCircle(
                            color = p.color,
                            radius = p.radius,
                            center = p.center,
                            style = Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
                        )

                        val textToDraw = p.text

                        val textLayoutResult = textMeasurer.measure(textToDraw, textStyle)
                        drawText(
                            textMeasurer = textMeasurer,
                            text = textToDraw,
                            style = textStyle.copy(color = p.color),
                            topLeft = Offset(
                                x = p.center.x - textLayoutResult.size.width / 2,
                                y = p.center.y + p.radius,
                            )
                        )
                    } else if (p.type == "empty") {
                        drawCircle(
                            color = p.color,
                            radius = p.radius/2,
                            center = p.center,
                            style = Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
                        )
                    }

                }

            }

        }



}
