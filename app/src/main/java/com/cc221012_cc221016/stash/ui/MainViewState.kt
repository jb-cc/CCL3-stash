package com.cc221012_cc221016.stash.ui

import com.cc221012_cc221016.stash.data.Entries

data class MainViewState(
    val students: List<Entries> = emptyList(),
    val selectedScreen: Screen = Screen.First,
    val openDialog: Boolean = false
)