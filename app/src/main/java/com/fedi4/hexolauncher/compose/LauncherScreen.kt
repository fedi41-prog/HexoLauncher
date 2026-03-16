package com.fedi4.hexolauncher.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fedi4.hexolauncher.LauncherViewModel
import com.fedi4.hexolauncher.ui.theme.HexoLauncherTheme
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState

@Composable
fun LauncherScreen(modifier: Modifier, vm: LauncherViewModel = LauncherViewModel()) {
    val hazeState = rememberHazeState()
    val context = LocalContext.current


    Column(
        modifier = modifier.fillMaxSize()
            .padding(bottom = Dp(50f))
            .padding(16.dp)
            //.hazeSource(hazeState)

    ) {
        Spacer(modifier = Modifier.weight(1f)
            //.hazeEffect(state = hazeState, style = HazeMaterials.ultraThin())
        )
        PatternPad(
            state = vm.padState,
            onPattern = vm::onPatternFinished,
            patternRoot = vm.tree.root
        )
    }

}

@Preview(showBackground = true)
@Composable
fun LauncherPreview() {


    LauncherScreen(modifier = Modifier, vm = LauncherViewModel())

}