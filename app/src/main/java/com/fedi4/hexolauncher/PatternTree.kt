package com.fedi4.hexolauncher

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewmodel.viewModelFactory

class PatternTree {
    val root = PatternNode.Folder(
        "Root",
        mutableMapOf(
            1 to PatternNode.Folder(
                "Social",
                mutableMapOf(
                    6 to PatternNode.App(
                        "Whatsapp",
                        "com.whatsapp"
                        ),
                    2 to PatternNode.App(
                        "Telegram",
                        "org.telegram.messenger.web"
                        )
                )
            ),
            0 to PatternNode.Folder(
                "Favourites",
                mutableMapOf(
                    1 to PatternNode.App(
                        "Habitica",
                        "com.habitica.app"
                    ),
                    2 to PatternNode.App(
                        "Chrome",
                        "com.android.chrome"
                    ),
                    3 to PatternNode.App(
                        "Email",
                        "com.google.android.gm"
                    ),
                    4 to PatternNode.App(
                        "Play Store",
                        "com.android.vending"
                    ),
                    5 to PatternNode.App(
                        "Spotify",
                        "com.spotify.music"
                    ),
                    6 to PatternNode.Folder(
                        "Others",
                        mutableMapOf(
                            0 to PatternNode.App(
                                "Google Maps",
                                "com.google.android.apps.maps"
                            ),
                        )
                    ),
                )
            )
        )
    )
}





sealed class PatternNode(
    open val name: String,
) {
    class Folder(
        override val name: String,
        val children: MutableMap<Int, PatternNode> = mutableMapOf(),
        val icon: String? = null
    ) : PatternNode(name)

    class Action(
        override val name: String,
        val launch: () -> Unit,
        val icon: String? = null
    ) : PatternNode(name)

    class App(
        override val name: String,
        val packageName: String? = null
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