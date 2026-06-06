package com.fedi4.hexolauncher.core.ui.components

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fedi4.hexolauncher.core.data.PadPoint
import com.fedi4.hexolauncher.core.data.PadPointType
import com.fedi4.hexolauncher.core.ui.HexoPadViewModel
import com.fedi4.hexolauncher.core.ui.layout.HexoLayout
import com.fedi4.hexolauncher.core.ui.layout.HexoLayoutController
import kotlin.math.min


@Composable
fun PatternPad(
    modifier: Modifier = Modifier,
    vm: HexoPadViewModel,
    layoutSize: Dp
) {

    val controller = remember { HexoLayoutController() }

    val itemSize = (layoutSize / 3.2f)
    val iconSize = (itemSize * 0.8f)
    val radius = ((layoutSize-itemSize) / 2)



    // MORPH STUFF
//    val shapeA = remember {
//        RoundedPolygon.circle(
//            6,
//        )
//    }
//    val shapeB = remember {
//        RoundedPolygon(
//            numVertices = 6,
//            rounding = CornerRounding(0.2f)

//        )
//    }
//    val morph = remember {
//        Morph(shapeA, shapeB)
//    }

    val animatedProgress = animateFloatAsState(
        targetValue = if (!vm.isDragging.value) 1f else 0.5f,
        label = "progress",
        animationSpec = spring(2f, Spring.StiffnessHigh)
    )

    // ===================



    HexoLayout(
        modifier = modifier
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        // handle pointer event
                        if (event.type == PointerEventType.Press) {
                            Log.d("Pointer", event.type.toString())
                            vm.onDragStart()
                            controller.send(event.changes.first().position)
                        }
                        if (event.type == PointerEventType.Release) {
                            Log.d("Pointer", event.type.toString())
                            vm.onDragEnd()
                        }
                    }
                }
            }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset -> //vm.onDragStart() }, onDragEnd = {
                            vm.onDragEnd() },
                        onDragCancel = {
                            vm.onDragCancel() },
                        onDrag = { change, dragAmount ->
                        }
                    )
                }.border(
                3.dp,
                MaterialTheme.colorScheme.surface.copy(1 - animatedProgress.value),
                CircleShape
                ),
        itemSize = itemSize,
        ringRadius = radius,
        onNearestChild = { id ->
            vm.onTouchedPoint(id) },
        onExitCircle = {
            vm.onDragCancel() },
        controller = controller
    ) {
        repeat(7) { i ->
            PatternPadPoint(
                size = iconSize,
                id = i,
                vm = vm
            )
        }
    }
}

@Composable
fun PatternPadWrapper(modifier: Modifier, vm: HexoPadViewModel) {

    var layoutSize by remember { mutableStateOf(0f) }

    val density = LocalDensity.current

    Box(
        modifier = modifier.onSizeChanged {
            layoutSize = min(it.width.toFloat(), it.height.toFloat())
        }
    ) {
        if (layoutSize > 0f) {
            val sizeDp = with(density) { layoutSize.toDp() }
            Box(Modifier.size(sizeDp).align(Alignment.Center)) {

                PatternTrailCanvas(
                    modifier = Modifier.size(sizeDp), vm
                )

                PatternPad(
                    modifier = Modifier.size(sizeDp),
                    vm = vm,
                    layoutSize = sizeDp
                )
            }
        }

    }
}

@Preview()
@Composable
fun PatternPadPreview() {
    PatternPadWrapper(
        modifier = Modifier,
        vm = HexoPadViewModel(context = LocalContext.current)
    )
}