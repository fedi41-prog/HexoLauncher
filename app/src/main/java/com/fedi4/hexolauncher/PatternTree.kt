package com.fedi4.hexolauncher

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewmodel.viewModelFactory

class PatternTree {
    val root = PatternNode.Folder(
        "Root",
        mutableMapOf(
            0 to PatternNode.Folder(
                "Social",
                mutableMapOf(

                    1 to PatternNode.Action(
                        "Whatsapp",
                        launch = {
                            startApp("com.whatsapp")
                        }),
                    2 to PatternNode.Action(
                        "Telegram",
                        launch = { startApp("org.telegram.messenger.web") })

                )
            )
        )
    )
}
sealed class PatternNode(
    open val name: String
) {

    class Folder(
        override val name: String,
        val children: MutableMap<Int, PatternNode> = mutableMapOf()
    ) : PatternNode(name)

    class Action(
        override val name: String,
        val launch: () -> Unit
    ) : PatternNode(name)
}

fun resolvePattern(
    root: PatternNode,
    pattern: List<Int>
): PatternNode? {
    var last: PatternNode = root
    var current: PatternNode = root

    for (step in pattern) {
        if (current is PatternNode.Folder) {
            last = current
            current = current.children[step] ?: return null
        } else if (last is PatternNode.Folder) {
            current = last.children[step] ?: return null
        }

    }
    return current
}
fun resolveWithTrace(
    root: PatternNode,
    pattern: List<Int>
): List<PatternNode> {
    val trace = mutableListOf<PatternNode>()
    var current: PatternNode = root

    for (step in pattern) {
        if (current is PatternNode.Folder) {
            current = current.children[step] ?: break
            trace.add(current)
        }
    }
    return trace
}