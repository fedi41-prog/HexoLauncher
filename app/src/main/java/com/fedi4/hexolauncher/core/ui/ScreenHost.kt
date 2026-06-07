package com.fedi4.hexolauncher.core.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fedi4.hexolauncher.core.ui.screens.AppScreen
import com.fedi4.hexolauncher.core.ui.screens.LauncherScreen


@Composable
fun ScreenHost(modifier: Modifier = Modifier) {

    val state = rememberPagerState { 2 }

    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = state
    ) { page ->
        when (page) {
            0 -> LauncherScreen()
            1 -> AppScreen()
        }
    }
}
