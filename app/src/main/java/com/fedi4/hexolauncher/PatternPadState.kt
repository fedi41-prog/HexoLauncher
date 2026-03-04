package com.fedi4.hexolauncher

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin

data class PadPoint(
    val index: Int,
    val center: Offset,

    val text: String,
    val color: Color = Color.White,
    val visibility: Boolean = true
)

class PatternPadState {

    val points = mutableStateListOf<PadPoint>()
    val pattern = mutableStateListOf<Int>()
    var dragPos = mutableStateOf<Offset?>(null)


    fun setPoints(list: List<PadPoint>) {
        points.clear()
        points.addAll(list)
    }

    fun onStart(pos: Offset) {
        dragPos.value = pos
        hitTest(pos)
    }

    fun onDrag(pos: Offset) {
        dragPos.value = pos
        hitTest(pos)
    }

    fun onEnd(onPattern: (List<Int>) -> Unit) {
        if (pattern.isNotEmpty()) {
            onPattern(pattern.toList())
        }
        reset()
    }

    fun reset() {
        pattern.clear()
        dragPos.value = null
    }

    private fun hitTest(pos: Offset) {
        val nearestPoint = points.minBy { (it.center - pos).getDistance() }
        if (pattern.isEmpty()) {
            pattern.add(nearestPoint.index)
        }
        val last = pattern.last()
        if (pattern.last() != nearestPoint.index && getNeighbors(last).contains(nearestPoint.index)) {
            pattern.add(nearestPoint.index)
        }
    }
}
fun getNeighbors(id: Int): List<Int> {
    if (id == 0) return listOf(1, 2, 3, 4, 5, 6)
    if (id == 6) return listOf(0, 1, 5)
    if (id == 1) return listOf(0, 2, 6)

    return listOf((id - 1), (id + 1), 0)
}

fun generatePoints(
    canvasSize: Size,
    root: PatternNode.Folder,
    currentPattern: List<Int> = emptyList()
): List<PadPoint> {
    val center = Offset(canvasSize.width / 2, canvasSize.height / 2)
    val ringRadius = canvasSize.minDimension * 0.35f

    val points = mutableListOf<PadPoint>()

    fun getPointColor(node: PatternNode?): Color {
        if (node is PatternNode.Action) {
            return Color.Green
        } else if (node is PatternNode.Folder) {
            return Color.Blue
        }
        return Color.White
    }


    val trace = resolveWithTrace(root, currentPattern)
    var nodes = mapOf<Int, PatternNode>()


    if (trace.isEmpty()) {

        nodes = root.children

    } else {
        var currentFolder = trace.findLast { it is PatternNode.Folder }
        if (currentFolder is PatternNode.Folder) {
            nodes = currentFolder.children
        }
    }

    points.add(PadPoint(0, center, nodes[0]?.name ?: "NONE", getPointColor(nodes[0]), visibility = nodes.contains(0)))

    for (i in 0 until 6) {
        val angle = Math.toRadians(60.0 * i - 90.0)
        val x = center.x + cos(angle).toFloat() * ringRadius
        val y = center.y + sin(angle).toFloat() * ringRadius
        points.add(PadPoint(i + 1, Offset(x, y), nodes[i+1]?.name ?: "NONE", getPointColor(nodes[i+1]), visibility = nodes.contains(i+1)))
    }
    return points
}