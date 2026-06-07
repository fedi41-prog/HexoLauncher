package com.fedi4.hexolauncher.core.ui.components.patternpad

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fedi4.hexolauncher.core.ui.HexoPadViewModel
import com.fedi4.hexolauncher.core.ui.layout.HexoLayout
import com.fedi4.hexolauncher.core.ui.layout.HexoLayoutController
import kotlin.math.min
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fedi4.hexolauncher.core.ui.PatternPadGeometry
import com.fedi4.hexolauncher.core.ui.PatternPadUiState


@Composable
fun PatternPad(
    modifier: Modifier = Modifier,
    vm: HexoPadViewModel = viewModel(factory = HexoPadViewModel.Factory)
) {
    val uiState = vm.uiState
    val geometry = uiState.geometry.value

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
        targetValue = if (!uiState.isDragging.value) 1f else 0.5f,
        label = "progress",
        animationSpec = spring(2f, Spring.StiffnessHigh)
    )

    // ===================



    HexoLayout(

        // MODIFIER
        modifier = modifier
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        uiState.pointerPosition.value = event.changes.first().position
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
                animatedProgress.value.dp*10,
                Color.Black.copy(1 - animatedProgress.value),
                CircleShape
                ),

        // PARAMETERS
        itemSize = geometry.itemSizeDp,
        ringRadius = geometry.radiusDp,
        onNearestChild = { id ->
            vm.onTouchedPoint(id) },
        onExitCircle = {
            vm.onDragCancel() },
        controller = controller
    ) {
        // PATTERN PAD POINTS
        repeat(7) { i ->
            PatternPadPoint(
                size = geometry.iconSizeDp /** (1.5f - animatedProgress.value)*/,
                id = i,
                vm = vm
            )
        }
    }
}

@Composable
fun PatternPadWrapper(modifier: Modifier,     vm: HexoPadViewModel = viewModel(factory = HexoPadViewModel.Factory)) {

    LaunchedEffect(vm) {
        vm.loadPatternRoot()
    }

    val density = LocalDensity.current

    Box(
        modifier = modifier.onSizeChanged {
            vm.uiState.geometry.value = PatternPadGeometry.fromLayoutSize(min(it.width.toFloat(), it.height.toFloat()))
        }
    ) {
        if (vm.uiState.geometry.value.layoutSize > 0f) {
            val sizeDp = with(density) { vm.uiState.geometry.value.layoutSize.toDp() }
            Box(Modifier.size(sizeDp).align(Alignment.Center)) {

                PatternTrailCanvas(
                    modifier = Modifier.size(sizeDp),
                    vm = vm,
                )

                PatternPad(
                    modifier = Modifier.size(sizeDp),
                    vm = vm,
                )
            }
        }

    }
}

@Preview()
@Composable
fun PatternPadPreview() {
    PatternPadWrapper(
        modifier = Modifier
    )
}