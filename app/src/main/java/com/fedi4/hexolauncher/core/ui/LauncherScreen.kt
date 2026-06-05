package com.fedi4.hexolauncher.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fedi4.hexolauncher.core.ui.components.PatternPad

@Composable
fun LauncherScreen(modifier: Modifier, vm: HexoPadViewModel) {
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
            modifier = Modifier.weight(1f),
            vm = vm
        )
    }

}

