 package com.fedi4.hexolauncher

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
        enableEdgeToEdge()
        setContent {
            HexoLauncherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (
                        modifier = Modifier.padding(innerPadding),
                        verticalArrangement = Arrangement.Bottom
                        ){
                        Spacer(modifier = Modifier.weight(1f))

                        LauncherScreen()
                        //InstalledLaunchableAppsDebug()

                    }
                }
            }
        }

    }
}

 @Composable
 fun LauncherScreen(vm: LauncherViewModel = LauncherViewModel()) {
     PatternPad(
         state = vm.padState,
         onPattern = vm::onPatternFinished,
         patternRoot = vm.tree.root
     )
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
        LauncherScreen(vm = LauncherViewModel())
    }
}