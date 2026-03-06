 package com.fedi4.hexolauncher

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.core.graphics.drawable.toBitmap
import com.fedi4.hexolauncher.ui.theme.HexoLauncherTheme

class MainActivity : ComponentActivity() {

    init {
        instance = this
    }

    companion object {
        private var instance: MainActivity? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lightTransparentStyle = SystemBarStyle.light(
            scrim = TRANSPARENT,
            darkScrim = TRANSPARENT
        )
        enableEdgeToEdge(
            statusBarStyle = lightTransparentStyle,
            navigationBarStyle = lightTransparentStyle
        )

        //window.addFlags(WindowManager.LayoutParams.)


        setContent {
            MaterialTheme {

                    LauncherScreen(modifier = Modifier.padding().background(color = Color.Transparent))


            }
        }

    }
}

 @Composable
 fun LauncherScreen(modifier: Modifier, vm: LauncherViewModel = LauncherViewModel()) {
     val context = LocalContext.current

     Column (modifier = Modifier.fillMaxSize().background(color = Color.Transparent).padding(bottom = Dp(50f)))
     {
         Spacer(modifier = Modifier.weight(1f))
         PatternPad(
             state = vm.padState,
             onPattern = vm::onPatternFinished,
             patternRoot = vm.tree.root
         )
     }
 }




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HexoLauncherTheme {
        LauncherScreen(modifier = Modifier, vm = LauncherViewModel())
    }
}