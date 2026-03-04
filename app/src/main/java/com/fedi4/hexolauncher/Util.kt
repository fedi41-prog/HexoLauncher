package com.fedi4.hexolauncher

import androidx.compose.ui.graphics.painter.BitmapPainter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.graphics.drawable.toBitmap

fun startApp(packageName: String) {
    Log.d("Pattern", "Starting app $packageName")
    val context: Context = MainActivity.applicationContext()
    val intent = context.packageManager
        .getLaunchIntentForPackage(packageName)
        ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    intent?.let { context.startActivity(it) }
}

@Composable
fun IconImage(
    drawable: Drawable?,
    size: Dp = 48.dp
) {
    drawable?.let {
        Image(
            painter = BitmapPainter(it.toBitmap().asImageBitmap()),
            contentDescription = null,
            modifier = Modifier.size(size)
        )
    }
}

fun loadAppIcon(
    pm: PackageManager,
    packageName: String
): Drawable? {
    return try {
        pm.getApplicationIcon(packageName)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}

@Composable
fun InstalledLaunchableAppsDebug() {
    val context = LocalContext.current
    val pm = context.packageManager

    val apps = remember {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        pm.queryIntentActivities(intent, 0)
            .map {
                val label = it.loadLabel(pm).toString()
                val pkg = it.activityInfo.packageName
                val icon = it.loadIcon(pm)
                Triple(label, pkg, icon)
            }
            .sortedBy { it.first.lowercase() }
    }

    LazyColumn {
        items(apps) { (name, pkg, icon) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconImage(icon, size = 40.dp)

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(name, fontSize = 16.sp)
                    Text(pkg, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}