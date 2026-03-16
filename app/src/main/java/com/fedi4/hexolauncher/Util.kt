package com.fedi4.hexolauncher

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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.createBitmap
import com.fedi4.hexolauncher.MainActivity.Companion.applicationContext

fun startApp(packageName: String) {
    Log.d("Pattern", "Starting app $packageName")
    val context: Context = applicationContext()
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


