package com.fedi4.hexolauncher.appsorter.compose


import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fedi4.hexolauncher.core.data.PatternNode
import com.fedi4.hexolauncher.R
import com.fedi4.hexolauncher.appsorter.AppInfo
import com.fedi4.hexolauncher.appsorter.AppSorterViewModel
import com.fedi4.hexolauncher.core.util.getCircleBitmap

@Composable
fun AppSorterScreen (
    modifier: Modifier = Modifier,
    viewModel: AppSorterViewModel
) {
    val apps = viewModel.loadAllApps()
    val roundedIcons = viewModel.loadRoundedIcons()


    Row {
        Column (modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.height(60.dp).fillMaxWidth().background(Color.White)) {
                Text("Apps", fontSize = 24.sp, modifier = Modifier.align(Alignment.Center))
            }
            LazyColumn(modifier = Modifier.weight(1f)) {

                items(apps.size) {
                    if (it == 0) Spacer(modifier = Modifier.height(20.dp))
                    AppListItem(appInfo = apps[it])
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
        Column (modifier = Modifier.weight(1f).background(Color.White.copy(0.6f))) {
            Box(modifier = Modifier.height(60.dp).fillMaxWidth().background(Color.White)) {
                Text("Patterns", fontSize = 24.sp, modifier = Modifier.align(Alignment.Center))
            }

            Column(modifier = Modifier.weight(1f)) {

                Spacer(modifier = Modifier.height(20.dp))
                for (i in 0 until 7) {

                    PatternListItem(modifier = Modifier, node = PatternNode.Folder("Folder $i"), idx = i, vm = viewModel)

                }
            }
        }
    }

    
}

@Composable
fun PatternListItem(
    modifier: Modifier = Modifier,
    node: PatternNode,
    idx: Int,
    vm: AppSorterViewModel
) {
    val context = LocalContext.current

    val folderRound = remember {
        val bitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.folder_with_bg
        ).asImageBitmap()

        getCircleBitmap(bitmap)
    }
    Row (modifier.padding(start = 10.dp).padding(vertical = 10.dp)){

        Text("$idx.", fontSize = 16.sp, modifier = Modifier.align(Alignment.CenterVertically).padding(end = 10.dp))


        if (node is PatternNode.Folder) {
            Image(
                folderRound, contentDescription = node.name,
                modifier = Modifier.size(48.dp)
            )
        }
        else if (node is PatternNode.App) {
            Image(
                vm.roundedIcons[node.packageName]!!, contentDescription = node.name,
                modifier = Modifier.size(48.dp)
            )
        }
        else {
            Spacer(modifier = Modifier.size(48.dp))
        }
    }

}



@Composable
fun AppListItem(
    modifier: Modifier = Modifier,
    appInfo: AppInfo,
) {
    Row (modifier.padding(start = 10.dp)){

        Image(

            appInfo.icon, contentDescription = appInfo.name,
            modifier = Modifier.size(48.dp)
        )

        Text(appInfo.name, fontSize = 10.sp, modifier = Modifier.align(Alignment.CenterVertically).padding(start = 10.dp))

    }
}


