package com.cc221012_cc221016.stash.ui.state

import com.cc221012_cc221016.stash.data.Entries
import com.cc221012_cc221016.stash.data.Users
import com.cc221012_cc221016.stash.ui.views.Screen

data class MainViewState(
    val entries: List<Entries> = emptyList(),
    val users: List<Users> = emptyList(),
    val selectedScreen: Screen = Screen.First,
    val openDialog: Boolean = false
)