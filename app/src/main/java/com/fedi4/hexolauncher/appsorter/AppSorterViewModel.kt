package com.fedi4.hexolauncher.appsorter

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fedi4.hexolauncher.core.data.AppTreeNode
import com.fedi4.hexolauncher.core.data.JsonPatternRepository
import com.fedi4.hexolauncher.core.data.PatternNode
import com.fedi4.hexolauncher.core.data.PatternRepository
import com.fedi4.hexolauncher.core.util.getCircleBitmap
import java.lang.Math.random

class AppSorterViewModel(
    private val patternRepository: PatternRepository
) : ViewModel() {

    val appTreeRoot: AppTreeNode.Folder = AppTreeNode.Folder("Root")
    var patternTreeRoot: PatternNode.Folder = PatternNode.Folder("Root")
    var apps: MutableList<AppInfo> = mutableListOf()
    var roundedIcons: MutableMap<String, ImageBitmap> = mutableMapOf()



    suspend fun loadAllApps(appContext: Context): List<AppInfo> {
        if (!apps.isEmpty()) return apps

        val pm = appContext.packageManager

        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val resolveInfos = pm.queryIntentActivities(intent, 0)

        apps = resolveInfos.map {
            val info = it.activityInfo.applicationInfo

            AppInfo(
                getCircleBitmap(it.loadIcon(pm).toBitmap(), 2).asImageBitmap(),
                it.loadLabel(pm).toString(),
                info.packageName
            )
        } as MutableList<AppInfo>

        return apps
    }

    suspend fun savePatternRoot() {
        patternRepository.savePattern(patternTreeRoot)
    }

    fun setTestPatternTree() {
        patternTreeRoot = PatternNode.Folder(
            "Root",
            buildMap {

                for (i in 0..6) {
                    
                    put(i, PatternNode.Folder(
                        "Folder $i",
                        buildMap {
                            for (j in 0..6) {
                                if (random() > 0.5) {
                                put(j, PatternNode.Folder(
                                    "Folder $j",
                                    buildMap {
                                        for (k in 0..6) {
                                            val app = apps.random()
                                            put(k, PatternNode.App(app.name, app.packageName))
                                        }
                                    }
                                ))
                                } else {                                            val app = apps.random()
                                    put(j, PatternNode.App(app.name, app.packageName))
                                }
                            }
                        }
                    ))
                    
                }

            }
        )
    }
    fun loadRoundedIcons(): MutableMap<String, ImageBitmap> {
        if (!roundedIcons.isEmpty()) return roundedIcons

        for (app in apps) {
            roundedIcons[app.packageName] = getCircleBitmap(app.icon, 2)
        }

        return roundedIcons
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val application =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]

                AppSorterViewModel(
                    patternRepository = JsonPatternRepository(application!!)
                )
            }
        }
    }
}