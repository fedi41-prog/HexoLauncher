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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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


@Composable
fun PatternPad(
    modifier: Modifier = Modifier,
    vm: HexoPadViewModel
) {

    val controller = remember { HexoLayoutController() }




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
        itemSize = 100.dp,
        ringRadius = 120.dp,
        onNearestChild = { id ->
            vm.onTouchedPoint(id) },
        onExitCircle = {
            vm.onDragCancel() },
        controller = controller
    ) {
        repeat(7) { i ->
            PatternPadPoint(
                size = 85.dp,
                id = i,
                vm = vm
            )
        }
    }
}


@Composable
fun PatternPadWrapper(modifier: Modifier, vm: HexoPadViewModel) {

    var padSize by remember { mutableStateOf(IntSize.Zero) }

    Box(modifier = modifier.size(
        width = with(LocalDensity.current) { padSize.width.toDp() },
        height = with(LocalDensity.current) { padSize.height.toDp() }
    )) {


        PatternTrailCanvas(
            Modifier.size(
                width = with(LocalDensity.current) { padSize.width.toDp() },
                height = with(LocalDensity.current) { padSize.height.toDp() }
            )
        )

        PatternPad(
            modifier = Modifier.fillMaxSize().onSizeChanged {padSize = it}, vm = vm
        )
    }
}

@Composable
fun PatternPadPoint(
    size: Dp,
    id: Int,
    vm: HexoPadViewModel
) {
    if (vm.points.isEmpty()) vm.updatePoints()
    val pointData: PadPoint = vm.points[id]

    val isLast = (!vm.currentPattern.isEmpty() && vm.currentPattern.last() == id)


    // MORPH STUFF
    //val shapeA = remember {
    //    RoundedPolygon.circle(
    //        6,
    //    )
    //}
    //val shapeB = remember {
    //    RoundedPolygon(
    //        numVertices = 6,
    //        rounding = CornerRounding(0.2f),
    //    )
    //}
    //val morph = remember {
    //    Morph(shapeA, shapeB)
    //}
    val animationTouchingPoint = animateFloatAsState(
        targetValue = if (isLast) 3f else 0f,
        label = "progress",
        animationSpec = spring(2f, Spring.StiffnessMedium)
    )
    val animationDragging = animateFloatAsState(
        targetValue = if (!vm.isDragging.value) 1f else 0.5f,
        label = "progress",
        animationSpec = spring(2f, Spring.StiffnessHigh)
    )

    // ===================

    val bgColor = Color.Black.copy(1-animationDragging.value)

    Box(Modifier.size(size)) {
        if (pointData.type == PadPointType.APP) {
            Image(
                painter = BitmapPainter(vm.getIcon(pointData.icon!!)),
                contentDescription = null,
                modifier = Modifier
                    .size(size)
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .border(3.dp + animationTouchingPoint.value.dp, Color.White, CircleShape)
                    .background(bgColor, shape = CircleShape)
            )
        } else if (pointData.type == PadPointType.FOLDER) {
            Box(
                Modifier
                    .size(size)
                    .align(Alignment.Center)
                    .border(3.dp + animationTouchingPoint.value.dp, Color.White, CircleShape)//.padding(size/5)
                    .background(bgColor, shape = CircleShape)
            ) {
                Text(pointData.text, fontSize = 10.sp, color = Color.White.copy(1.65f-animationDragging.value), modifier = Modifier.align(Alignment.Center))
            }
        } else if (pointData.type == PadPointType.ACTION) { }
        else if (pointData.type == PadPointType.EMPTY) {
            Box(
                Modifier
                    .size(size)
                    .align(Alignment.Center)
                    .border(1.dp + animationTouchingPoint.value.dp, Color.White, CircleShape)
                    .background(bgColor, shape = CircleShape)
            )
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