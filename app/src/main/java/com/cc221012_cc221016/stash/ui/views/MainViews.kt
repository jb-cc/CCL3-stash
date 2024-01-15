package com.cc221012_cc221016.stash.ui.views

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
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
    // You can replace this with the actual value from your ViewModel or state
    val loggedIn = true
    val hasUser = false

    if (!loggedIn){ LoginRegisterView(hasUser) }
    else { HomeView()  }
}