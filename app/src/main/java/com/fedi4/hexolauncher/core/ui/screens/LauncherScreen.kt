package com.fedi4.hexolauncher.core.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fedi4.hexolauncher.core.ui.LauncherViewModel
import com.fedi4.hexolauncher.core.ui.components.patternpad.PatternPadWrapper
import com.fedi4.hexolauncher.core.ui.components.TimeWidget
import kotlinx.coroutines.launch

@Composable
fun LauncherScreen(modifier: Modifier = Modifier, vm: LauncherViewModel = viewModel()) {
    val haptics = LocalHapticFeedback.current
    MyPager()

    val columnState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    fun updateList() {
        scope.launch {
            if (vm.editMode.value) {
                columnState.animateScrollToItem(2)
            } else {
                columnState.animateScrollToItem(0)
            }
        }
    }

    Box(modifier) {
        LazyColumn(
            modifier = Modifier,
            state = columnState,
            userScrollEnabled = false,
        ) {
            items(3) { page ->
                when (page) {
                    0 -> TimeWidget(modifier = Modifier.fillParentMaxHeight(0.5f))
                    1 -> PatternPadWrapper(
                        modifier = Modifier.fillParentMaxHeight(0.5f).fillMaxWidth().padding(16.dp)
                    )

                    2 -> AppScreen(modifier = Modifier.fillParentMaxHeight(0.5f).fillMaxWidth())
                }
            }
        }
        Box(Modifier.padding(20.dp).padding(vertical = 40.dp).align(Alignment.TopEnd)) {

            Text("Edit", modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        vm.editMode.value = !vm.editMode.value
                        updateList()
                    }
                )
            },
                color = Color.White, style = TextStyle(
                shadow = Shadow(Color.Black, Offset.Zero, 4f))
            )

        }
    }



//    Column(
//        modifier = modifier.fillMaxSize()
//            .padding(bottom = Dp(50f))
//            .padding(16.dp)
//            .background(color = androidx.compose.ui.graphics.Color.Transparent)
//            .combinedClickable(
//                onClick = {},
//                onLongClick = {
//                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
//                    vm.editMode.value = !vm.editMode.value
//                },
//                //onLongClickLabel = stringResource(R.string.open_context_menu)
//            )
//    ) {
//
//        Spacer(modifier = Modifier.weight(0.7f))
//        PatternPadWrapper(
//            modifier = Modifier.weight(1.1f).fillMaxWidth().align(Alignment.CenterHorizontally)
//        )
//    }
}


@Composable
fun MyPager(
    modifier: Modifier = Modifier,
    item1: @Composable () -> Unit = {},
    item2: @Composable () -> Unit = {},
    item3: @Composable () -> Unit = {},
) {

}


@Preview()
@Composable
fun LauncherScreenPreview() {
    LauncherScreen(modifier = Modifier.fillMaxSize())
}

