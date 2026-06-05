package com.fedi4.hexolauncher.core.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.createBitmap
import com.fedi4.hexolauncher.core.data.PadPoint
import com.fedi4.hexolauncher.core.data.PadPointType
import com.fedi4.hexolauncher.core.data.PatternNode
import com.fedi4.hexolauncher.core.ui.MainActivity
import kotlin.math.cos
import kotlin.math.sin

fun startApp(packageName: String) {
    Log.d("Pattern", "Starting app $packageName")
    val context: Context = MainActivity.Companion.applicationContext()
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


// Source - https://stackoverflow.com/a/12089127
// Posted by Altaf
// Retrieved 2026-03-06, License - CC BY-SA 3.0
fun getCircleBitmap(bitmap: Bitmap, margin: Int = 0): Bitmap {
    val output = createBitmap(bitmap.getWidth(), bitmap.getHeight())
    val canvas: Canvas = Canvas(output)

    val color = -0xbdbdbe
    val paint: Paint = Paint()
    val rect: Rect = Rect(-margin, -margin, bitmap.getWidth()+margin, bitmap.getHeight()+margin)

    paint.setAntiAlias(true)
    canvas.drawARGB(0, 0, 0, 0)
    paint.setColor(color)
    // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
    canvas.drawCircle(
        (bitmap.getWidth() / 2).toFloat(), (bitmap.getHeight() / 2).toFloat(),
        (bitmap.getWidth() / 2 - margin).toFloat(), paint
    )
    paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
    canvas.drawBitmap(bitmap, rect, rect, paint)
    //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
    //return _bmp;
    return output
}
fun getCircleBitmap(bitmap: ImageBitmap, margin: Int = 0): ImageBitmap {
    val bitmap = bitmap.asAndroidBitmap()
    val output = createBitmap(bitmap.getWidth(), bitmap.getHeight())
    val canvas: Canvas = Canvas(output)

    val color = -0xbdbdbe
    val paint: Paint = Paint()
    val rect: Rect = Rect(-margin, -margin, bitmap.getWidth()+margin, bitmap.getHeight()+margin)

    paint.setAntiAlias(true)
    canvas.drawARGB(0, 0, 0, 0)
    paint.setColor(color)
    // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
    canvas.drawCircle(
        (bitmap.getWidth() / 2).toFloat(), (bitmap.getHeight() / 2).toFloat(),
        (bitmap.getWidth() / 2 - margin).toFloat(), paint
    )
    paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
    canvas.drawBitmap(bitmap, rect, rect, paint)
    //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
    //return _bmp;
    return output.asImageBitmap()
}

fun getPointNeighbors(id: Int): List<Int> {
    if (id == 0) return listOf(1, 2, 3, 4, 5, 6)
    if (id == 6) return listOf(0, 1, 5)
    if (id == 1) return listOf(0, 2, 6)

    return listOf((id - 1), (id + 1), 0)
}

fun hexoPosition(
    index: Int,
    center: Offset,
    ringRadius: Float
): Offset {

    if (index == 0) return center

    val i = index - 1
    val angle = Math.toRadians(60.0 * i - 90.0)

    val x = center.x + cos(angle).toFloat() * ringRadius
    val y = center.y + sin(angle).toFloat() * ringRadius

    return Offset(x, y)
}

fun convertToPadPoint(node: PatternNode?, index: Int): PadPoint {
    when (node) {
        is PatternNode.App -> return PadPoint(
            index = index,
            text = node.name,
            color = Color.White,
            visibility = true,
            icon = node.packageName,
            radius = 85f,
            type = PadPointType.APP
        )
        is PatternNode.Folder ->  return PadPoint(
            index = index,
            text = node.name,
            color = Color.White,
            visibility = true,
            icon = node.icon,
            radius = 85f,
            type = PadPointType.FOLDER
        )
        is PatternNode.Action -> return PadPoint(
            index = index,
            text = node.name,
            color = Color.White,
            visibility = true,
            icon = node.icon,
            radius = 85f,
            type = PadPointType.ACTION
        )
        null -> return PadPoint(
            index = index,
            text = "",
            color = Color.White,
            visibility = false,
            icon = null,
            radius = 85f,
            type = PadPointType.EMPTY
        )
    }
}