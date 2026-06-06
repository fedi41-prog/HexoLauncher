package com.fedi4.hexolauncher.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.sql.Time
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun TimeWidget(modifier: Modifier = Modifier) {

    var calendar by remember { mutableStateOf(Calendar.getInstance()) }

    LaunchedEffect(Unit) {
        while (true) {
            calendar = Calendar.getInstance()

            delay(1000)
        }
    }


    Box(
        modifier = modifier.fillMaxSize()
    ) {
        TimeDisplay(modifier = Modifier.align(Alignment.Center), calendar)
    }



}

@Composable
fun TimeDisplay(modifier: Modifier = Modifier, calendar: Calendar = Calendar.getInstance()) {

    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val minutes = calendar.get(Calendar.MINUTE)
    val seconds = calendar.get(Calendar.SECOND)

    val textStyle = remember {
        TextStyle(
            color = Color.White,
//            drawStyle = Stroke(10f, cap = StrokeCap.Round),
            fontSize = 80.sp,
            shadow = Shadow(
                color = Color.Black, offset = Offset(0f, 0f), blurRadius = 20f
            )
        ) }


    val textModifier = remember {
        Modifier//.padding(5.dp)
    }


    Column(modifier = modifier) {

        Text(hours.toString().padStart(2, '0'), style = textStyle, modifier= textModifier)
        Text(minutes.toString().padStart(2, '0'), style = textStyle, modifier= textModifier)
        //Text(seconds.toString().padStart(2, '0'), style = textStyle, modifier= textModifier)

    }
}

fun formatTime(seconds: Int): List<String> {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60

    return listOf(hours.toString().padStart(2, '0'), minutes.toString().padStart(2, '0'), remainingSeconds.toString().padStart(2, '0'))


}

@Preview(showBackground = true)
@Composable
fun TimeWidgetPreview() {
    TimeWidget()
}