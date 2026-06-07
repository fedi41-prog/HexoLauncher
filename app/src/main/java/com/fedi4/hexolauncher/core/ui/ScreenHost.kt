package com.fedi4.hexolauncher.core.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fedi4.hexolauncher.core.ui.screens.AppScreen
import com.fedi4.hexolauncher.core.ui.screens.LauncherScreen


@Composable
fun ScreenHost(modifier: Modifier = Modifier) {

    val state = rememberPagerState { 2 }
    val launcherVm = viewModel<LauncherViewModel>()

    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = state,
        userScrollEnabled = !launcherVm.editMode.value
    ) { page ->
        when (page) {
            0 -> LauncherScreen(vm = launcherVm)
            1 -> AppScreen()
        }
    }
}
