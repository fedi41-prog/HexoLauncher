package com.fedi4.hexolauncher.core.ui.components



import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fedi4.hexolauncher.core.util.IconImage

//@Composable
//private fun MaterialsCard(
//    name: String,
//    shape: Shape,
//    modifier: Modifier = Modifier,
//) {
//    val hazeState = rememberHazeState()
//    Card(
//        shape = shape,
//        colors = CardDefaults.cardColors(
//            containerColor = Color.Transparent,
//            contentColor = MaterialTheme.colorScheme.onSurface,
//        ),
//        modifier = modifier.size(160.dp),
//    ) {
//        Box(
//            Modifier
//                .fillMaxSize()
//                .hazeEffect(state = hazeState, style = HazeMaterials.ultraThin())
//                .padding(16.dp),
//        ) {
//            Text(name)
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MaterialsCardPreview() {
//    MaterialsCard("Card", MaterialTheme.shapes.medium)
//}



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