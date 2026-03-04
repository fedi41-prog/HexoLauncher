package com.fedi4.hexolauncher

import androidx.lifecycle.ViewModel

class LauncherViewModel : ViewModel() {

    val padState = PatternPadState()
    val tree = PatternTree()

    fun onPatternFinished(pattern: List<Int>) {
        when (val node = resolvePattern(tree.root, pattern)) {
            is PatternNode.Action -> node.launch()
            is PatternNode.Folder -> {

            }
            null -> {
                // Feedback
            }
        }
    }
}