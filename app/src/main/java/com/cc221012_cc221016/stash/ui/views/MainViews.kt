package com.cc221012_cc221016.stash.ui.views

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.cc221012_cc221016.stash.ui.views.Composables.AddEntryView
import com.cc221012_cc221016.stash.ui.views.Composables.LoginRegisterView
import com.cc221012_cc221016.stash.ui.views.Composables.HomeView

class MainViewModel : ViewModel() // Dummy ViewModel

sealed class Screen(val route: String){
    object First: Screen("first")
    object Second: Screen("second")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(mainViewModel: MainViewModel){
    //    LoginRegisterView(hasUser = false)
    //    LoginRegisterView(hasUser = true)
    //    HomeView()
    AddEntryView()

}