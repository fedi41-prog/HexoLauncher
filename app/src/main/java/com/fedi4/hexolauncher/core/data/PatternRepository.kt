package com.fedi4.hexolauncher.core.data

import android.content.Context
import kotlinx.serialization.json.Json


interface PatternRepository {
    suspend fun savePattern(tree: PatternNode)
    suspend fun getPattern():  PatternNode?
}

class JsonPatternRepository(context: Context) : PatternRepository {

    private val context = context.applicationContext
    private val json = Json {
        prettyPrint = true
        classDiscriminator = "type"
    }

    override suspend fun savePattern(tree: PatternNode) {
        val data = json.encodeToString(tree)
        context.openFileOutput("patternTree.json", Context.MODE_PRIVATE)
            .use { it.write(data.toByteArray()) }
    }

    override suspend fun getPattern(): PatternNode? {
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