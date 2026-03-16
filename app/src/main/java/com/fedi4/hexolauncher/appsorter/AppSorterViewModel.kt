package com.fedi4.hexolauncher.appsorter

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.fedi4.hexolauncher.MainActivity
import com.fedi4.hexolauncher.getCircleBitmap
import com.fedi4.hexolauncher.loadAppIcon

class AppSorterViewModel : ViewModel() {
    val appTreeRoot: AppTreeNode.Folder = AppTreeNode.Folder("Root")
    var apps: MutableList<AppInfo> = mutableListOf()
    var roundedIcons: MutableMap<String, ImageBitmap> = mutableMapOf()





    fun loadAllApps(context: Context): List<AppInfo> {
        if (!apps.isEmpty()) return apps

        val pm = context.packageManager

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
    fun loadRoundedIcons(): MutableMap<String, ImageBitmap> {
        if (!roundedIcons.isEmpty()) return roundedIcons

        for (app in apps) {
            roundedIcons[app.packageName] = getCircleBitmap(app.icon, 2)
        }

        return roundedIcons
    }
}