package com.fedi4.hexolauncher.core.data
import kotlinx.serialization.*
import kotlinx.serialization.json.Json


class SimplePattern(val pattern: List<Int>, val action: () -> Unit) {
    fun matching(pattern: List<Int>): Boolean {
        if (pattern.size != this.pattern.size) return false
        return pattern.zip(this.pattern).all { (a, b) -> a == b }
    }

    fun launchIfMatching(pattern: List<Int>): Boolean {
        if (matching(pattern)) {
            action()
            return true
        }
        return false
    }
}

@Serializable
sealed class PatternNode {

    abstract val name: String

    @Serializable
    @SerialName("folder")
    data class Folder(
        override val name: String,
        val children: Map<Int, PatternNode> = emptyMap(),
        val icon: String? = null
    ) : PatternNode()

    @Serializable
    @SerialName("action")
    data class Action(
        override val name: String,
        val actionId: Int, // statt lambda
        val icon: String? = null
    ) : PatternNode()

    @Serializable
    @SerialName("app")
    data class App(
        override val name: String,
        val packageName: String? = null
    ) : PatternNode()
}

fun PatternNode.toJson(json: Json): String {
    return json.encodeToString(this)
}
fun patternNodeFromJson(jsonString: String, json: Json): PatternNode {
    return json.decodeFromString(jsonString)
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
    root: PatternNode.Folder,
    pattern: List<Int>
): List<PatternNode> {
    val trace = mutableListOf<PatternNode>()
    var currentFolder: PatternNode.Folder = root
    var current: PatternNode = root


    for (step in pattern) {
        if (current is PatternNode.Folder) {
            currentFolder = current
            current = currentFolder.children[step] ?: continue
            trace.add(current)
        }
        else {
            current = currentFolder.children[step] ?: continue
            trace.add(current)
        }
    }
    return trace
}