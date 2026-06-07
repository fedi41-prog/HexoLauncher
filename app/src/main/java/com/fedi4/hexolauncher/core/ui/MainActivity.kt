package com.fedi4.hexolauncher.core.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.fedi4.hexolauncher.core.ui.screens.LauncherScreen
import com.fedi4.hexolauncher.theme.AppTheme

class MainActivity : ComponentActivity() {

   init {
       instance = this
   }

   companion object {
       private var instance: MainActivity? = null

       fun applicationContext() : Context {
           return instance!!.applicationContext
       }
       fun getActivity(): ComponentActivity {
           return instance!!
       }
   }

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       val lightTransparentStyle = SystemBarStyle.Companion.light(
           scrim = Color.TRANSPARENT,
           darkScrim = Color.TRANSPARENT
       )
       enableEdgeToEdge(
           statusBarStyle = lightTransparentStyle,
           navigationBarStyle = lightTransparentStyle
       )



       setContent {
           AppTheme {
               Box(Modifier.Companion.fillMaxSize()) {
                   ScreenHost()
               }
           }
       }

   }
}