package com.fedi4.hexolauncher.core.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fedi4.hexolauncher.core.data.PadPoint
import com.fedi4.hexolauncher.core.data.PatternNode
import com.fedi4.hexolauncher.core.data.PatternStorage
import com.fedi4.hexolauncher.core.data.resolveWithTrace
import com.fedi4.hexolauncher.core.util.convertToPadPoint
import com.fedi4.hexolauncher.core.util.getCircleBitmap
import com.fedi4.hexolauncher.core.util.loadAppIcon
import com.fedi4.hexolauncher.core.util.startApp

class HexoPadViewModel() : ViewModel() {
    var patternRoot: PatternNode.Folder = PatternNode.Folder("Root")


    val points = mutableStateListOf<PadPoint>()
    val currentPattern = mutableStateListOf<Int>()
    val icons: MutableMap<String, ImageBitmap> = mutableMapOf()
    val isDragging = mutableStateOf(false)
    val pointerPosition = mutableStateOf(Offset.Zero)

    fun getIcon(packageName: String, appContext: Context): ImageBitmap {
        if (icons.containsKey(packageName)) return icons.getValue(packageName)
        val drawable = loadAppIcon(appContext.packageManager, packageName)
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
            val currentFolder = trace.findLast { it is PatternNode.Folder }
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

    fun loadPatternRoot(appContext: Context): PatternNode.Folder {
        val p = PatternStorage.load(appContext)
        if (p is PatternNode.Folder) patternRoot = p
        return patternRoot
    }
    fun savePatternRoot(appContext: Context) {
        PatternStorage.save(appContext, patternRoot)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HexoPadViewModel()
            }
        }
    }

}



data class PatternPadUiState(
    val points: List<PadPoint> = listOf(),
    val currentPattern: List<Int> = listOf(),
    val isDragging: Boolean = false
)