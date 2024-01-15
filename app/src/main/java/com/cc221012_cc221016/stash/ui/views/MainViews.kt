package com.cc221012_cc221016.stash.ui.views

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.cc221012_cc221016.stash.ui.views.Composables.AddEntryView
import com.cc221012_cc221016.stash.ui.views.Composables.HomeView
import com.cc221012_cc221016.stash.ui.views.Composables.LoginRegisterView
import com.cc221012_cc221016.stash.ui.views.Composables.ShowEntryView

class MainViewModel : ViewModel() // Dummy ViewModel

sealed class Screen(val route: String){
    object First: Screen("first")
    object Second: Screen("second")
}

@Composable
fun MainView(mainViewModel: MainViewModel){
//        LoginRegisterView(hasUser = false)
//        LoginRegisterView(hasUser = true)
//        HomeView()
//        AddEntryView()
        ShowEntryView()

}