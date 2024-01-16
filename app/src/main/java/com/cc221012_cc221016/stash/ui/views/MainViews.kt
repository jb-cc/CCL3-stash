package com.cc221012_cc221016.stash.ui.views

import androidx.compose.runtime.Composable
import com.cc221012_cc221016.stash.models.MainViewModel
import com.cc221012_cc221016.stash.ui.views.Composables.AddEntryView
import com.cc221012_cc221016.stash.ui.views.Composables.HomeView


sealed class Screen(val route: String){
    object First: Screen("first")
    object Second: Screen("second")
}

@Composable
fun MainView(mainViewModel: MainViewModel){
//        LoginRegisterView(hasUser = false)
//        LoginRegisterView(hasUser = true)
       HomeView(mainViewModel)
//  AddEntryView(mainViewModel)
//        ShowEntryView()
//        EditEntryView()
}