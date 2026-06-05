package com.fedi4.hexolauncher.core.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.fedi4.hexolauncher.core.data.PadPoint
import com.fedi4.hexolauncher.core.data.PatternNode
import com.fedi4.hexolauncher.core.data.PatternStorage
import com.fedi4.hexolauncher.core.data.resolveWithTrace
import com.fedi4.hexolauncher.core.util.convertToPadPoint
import com.fedi4.hexolauncher.core.util.getCircleBitmap
import com.fedi4.hexolauncher.core.util.loadAppIcon
import com.fedi4.hexolauncher.core.util.startApp

class HexoPadViewModel(context: Context) : ViewModel() {
    val appContext: Context = context.applicationContext

    var patternRoot: PatternNode.Folder = PatternNode.Folder("Root")


    val points = mutableStateListOf<PadPoint>()
    val currentPattern = mutableStateListOf<Int>()
    val icons: MutableMap<String, ImageBitmap> = mutableMapOf()
    val isDragging = mutableStateOf(false)


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


    fun onPatternFinished(pattern: List<Int>): Unit {
        val trace = resolveWithTrace(patternRoot, pattern)
        Log.d("Pattern", trace.toString())
        if (trace.isEmpty()) return

        val node = trace.last()

        when (node) {
            is PatternNode.Action -> Log.d("Pattern", "Action " + node.actionId.toString())
            is PatternNode.Folder -> {

            }

            is PatternNode.App -> {
                if (node.packageName != null) startApp(node.packageName)
            }

            null -> {}

        }
    }

    fun updatePoints() {
        points.clear()

        val trace = resolveWithTrace(patternRoot, currentPattern)

        var nodes = mapOf<Int, PatternNode>()


        if (trace.isEmpty()) {
            nodes = patternRoot.children
        } else {
            var currentFolder = trace.findLast { it is PatternNode.Folder }
            if (currentFolder is PatternNode.Folder) {
                nodes = currentFolder.children
            }
        }

        for (i in 0..6) {
            points.add(convertToPadPoint(nodes[i], i))
        }
    }

    fun onDragStart() {
        currentPattern.clear()
        updatePoints()
        isDragging.value = true
    }

    fun onTouchedPoint(id: Int) {
        if (!isDragging.value) return

        if (currentPattern.isEmpty() || currentPattern.last() != id) {
            currentPattern.add(id)
            Log.d("Pattern", currentPattern.toString())
        }

        updatePoints()
    }


    fun onDragEnd() {
        onPatternFinished(currentPattern)
        currentPattern.clear()
        updatePoints()
        isDragging.value = false
    }

    fun onDragCancel() {
        currentPattern.clear()
        updatePoints()
        isDragging.value = false
    }

    // SERIALIZATION

    fun loadPatternRoot(): PatternNode.Folder {
        val p = PatternStorage.load(appContext)
        if (p is PatternNode.Folder) patternRoot = p
        return patternRoot
    }
    fun savePatternRoot() {
        PatternStorage.save(appContext, patternRoot)
    }

}



data class PatternPadUiState(
    val points: List<PadPoint> = listOf(),
    val currentPattern: List<Int> = listOf(),
    val isDragging: Boolean = false
)