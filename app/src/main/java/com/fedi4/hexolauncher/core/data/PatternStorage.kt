package com.fedi4.hexolauncher.core.data

import android.content.Context
import kotlinx.serialization.json.Json

object PatternStorage {

    private val json = Json {
        prettyPrint = true
        classDiscriminator = "type"
    }

    fun save(context: Context, tree: PatternNode) {
        val data = json.encodeToString(tree)
        context.openFileOutput("patternTree.json", Context.MODE_PRIVATE)
            .use { it.write(data.toByteArray()) }
    }

    fun load(context: Context): PatternNode? {
        return try {
            val text = context.openFileInput("patternTree.json")
                .bufferedReader()
                .readText()

            json.decodeFromString(text)
        } catch (e: Exception) {
            null
        }
    }
}