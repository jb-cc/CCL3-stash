package com.cc221012_cc221016.stash.ui.state

import com.cc221012_cc221016.stash.ui.views.Screen

data class BccStudent(val name: String = "", val uid: String = "") // Dummy BccStudent

data class MainViewState(
    val students: List<BccStudent> = emptyList(),
    val selectedScreen: Screen = Screen.First,
    val openDialog: Boolean = false
)












//package com.cc221012_cc221016.stash.ui.state
//
//import com.cc221012_cc221016.stash.data.BccStudent
//import com.cc221012_cc221016.stash.ui.views.Screen
//
//data class MainViewState(
//    val students: List<BccStudent> = emptyList(),
//    val selectedScreen: Screen = Screen.First,
//    val openDialog: Boolean = false
//)