package com.cc221012_cc221016.stash.ui.views

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.cc221012_cc221016.stash.ui.views.Composables.AddEntryView

class MainViewModel : ViewModel() // Dummy ViewModel

sealed class Screen(val route: String){
    object First: Screen("first")
    object Second: Screen("second")
}

@Composable
fun MainView(mainViewModel: MainViewModel){
    //    LoginRegisterView(hasUser = false)
    //    LoginRegisterView(hasUser = true)
    //    HomeView()
    AddEntryView()

}