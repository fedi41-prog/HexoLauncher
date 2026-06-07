package com.fedi4.hexolauncher.core.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fedi4.hexolauncher.appsorter.AppSorterViewModel
import com.fedi4.hexolauncher.core.data.JsonPatternRepository
import com.fedi4.hexolauncher.core.data.PadPoint
import com.fedi4.hexolauncher.core.data.PatternNode
import com.fedi4.hexolauncher.core.data.PatternRepository
import com.fedi4.hexolauncher.core.data.resolveWithTrace
import com.fedi4.hexolauncher.core.ui.PatternPadUiState
import com.fedi4.hexolauncher.core.util.convertToPadPoint
import com.fedi4.hexolauncher.core.util.getCircleBitmap
import com.fedi4.hexolauncher.core.util.loadAppIcon
import com.fedi4.hexolauncher.core.util.startApp

class HexoPadViewModel(
    private val patternRepository: PatternRepository
) : ViewModel() {
    var patternRoot: PatternNode.Folder = PatternNode.Folder("Root")
    var uiState by mutableStateOf(PatternPadUiState())
        private set
    val icons: MutableMap<String, ImageBitmap> = mutableMapOf()
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
        uiState.points.clear()

        val trace = resolveWithTrace(patternRoot, uiState.currentPattern)

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
            uiState.points.add(convertToPadPoint(nodes[i], i))
        }
    }

    fun onDragStart() {
        uiState.currentPattern.clear()
        updatePoints()
        uiState.isDragging.value = true
    }

    fun onTouchedPoint(id: Int) {
        if (!uiState.isDragging.value) return

        if (uiState.currentPattern.isEmpty() || uiState.currentPattern.last() != id) {
            uiState.currentPattern.add(id)
            Log.d("Pattern", uiState.currentPattern.toString())
        }

        updatePoints()
    }


    fun onDragEnd() {
        onPatternFinished(uiState.currentPattern)
        uiState.currentPattern.clear()
        updatePoints()
        uiState.isDragging.value = false
    }

    fun onDragCancel() {
        uiState.currentPattern.clear()
        updatePoints()
        uiState.isDragging.value = false
    }

    // SERIALIZATION

    suspend fun loadPatternRoot(): PatternNode.Folder {
        val p = patternRepository.getPattern()
        if (p is PatternNode.Folder) patternRoot = p
        return patternRoot
    }

    suspend fun savePatternRoot() {
        patternRepository.savePattern(patternRoot)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val application =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]

                HexoPadViewModel(
                    patternRepository = JsonPatternRepository(application!!)
                )
            }
        }
    }
}