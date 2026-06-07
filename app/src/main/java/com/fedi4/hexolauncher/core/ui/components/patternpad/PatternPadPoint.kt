package com.fedi4.hexolauncher.core.ui.components.patternpad

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fedi4.hexolauncher.core.data.PadPoint
import com.fedi4.hexolauncher.core.data.PadPointType
import com.fedi4.hexolauncher.core.ui.HexoPadViewModel

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
                painter = BitmapPainter(vm.getIcon(pointData.icon!!, LocalContext.current)),
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
                    .border(5.dp + animationTouchingPoint.value.dp, Color.White, CircleShape)//.padding(size/5)
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