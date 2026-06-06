package com.fedi4.hexolauncher.core.ui.layout


import android.util.Log
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import com.fedi4.hexolauncher.core.util.hexoPosition
import java.util.Vector
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HexoLayout(
    modifier: Modifier = Modifier,
    itemSize: Dp,
    ringRadius: Dp,
    onNearestChild: (childIndex: Int) -> Unit,
    onExitCircle: () -> Unit,
    controller: HexoLayoutController,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val childCenters = remember { mutableStateMapOf<Int, Offset>() }
    val itemPx = with(density) { itemSize.roundToPx() }
    val radiusPx = with(density) { ringRadius.toPx() }
    val outerRadius = radiusPx + itemPx / 2
    val layoutSize = (radiusPx * 2 + itemPx).toInt()
    val center = Offset(layoutSize / 2f, layoutSize / 2f)

    fun process(pos: Offset) {
        if ((pos - center).getDistance() > outerRadius) {
            onExitCircle()
            return
        }

        childCenters.minByOrNull { (_, center) ->
            (pos - center).getDistance()
        }?.let { (index, _) ->
            onNearestChild(index)
        }
    }

    // expose function to outside
    LaunchedEffect(Unit) {
        controller.process = ::process
    }

    Layout(
        modifier = modifier.pointerInput(center, outerRadius) {
            detectDragGestures { change, dragAmount ->
                process(change.position)
            }
        },
        content = content
    ) { measurables, constraints ->



        val placeables = measurables.map {
            it.measure(Constraints.fixed(itemPx, itemPx))
        }


        layout(layoutSize, layoutSize) {
            val cx = layoutSize / 2f
            val cy = layoutSize / 2f

            placeables.forEachIndexed { index, p ->
                var (x,y) = hexoPosition(index, center, radiusPx)
                x -= itemPx / 2
                y -= itemPx / 2

                p.place(x.toInt(), y.toInt())
                childCenters[index] = Offset(x + itemPx / 2, y + itemPx / 2)
            }
        }
    }
}

class HexoLayoutController {
    var process: ((Offset) -> Unit)? = null

    fun send(pos: Offset) {
        process?.invoke(pos)
    }
}