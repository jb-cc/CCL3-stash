package com.cc221012_cc221016.stash.ui

import com.cc221012_cc221016.stash.BccStudent

data class MainViewState(
    val students: List<BccStudent> = emptyList(),
    val selectedScreen: Screen = Screen.First,
    val openDialog: Boolean = false
)