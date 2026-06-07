package com.fedi4.hexolauncher.core.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LauncherViewModel : ViewModel() {

    val editMode = mutableStateOf(false)

    fun toggleEditMode() {
        editMode.value = !editMode.value
    }

}