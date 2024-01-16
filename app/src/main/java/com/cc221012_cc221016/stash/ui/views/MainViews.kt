package com.cc221012_cc221016.stash.ui.views

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.cc221012_cc221016.stash.models.MainViewModel
import com.cc221012_cc221016.stash.ui.views.Composables.AddEntryView
import com.cc221012_cc221016.stash.ui.views.Composables.EditEntryView
import com.cc221012_cc221016.stash.ui.views.Composables.HomeView
import com.cc221012_cc221016.stash.ui.views.Composables.LoginRegisterView
import com.cc221012_cc221016.stash.ui.views.Composables.ShowEntryView



sealed class Screen(val route: String){
    object First: Screen("first")
    object Second: Screen("second")
}

@Composable
fun MainView(mainViewModel: MainViewModel){
    val mainViewState by mainViewModel.mainViewState.collectAsState()
    val users by mainViewModel.initialGetUsers().collectAsState(emptyList())
    Log.d("MainView", "MainView: $mainViewState")
    Log.d("MainView", "Got Users: $users")
    if (mainViewState.isUserAuthenticated) {
        HomeView()
    } else {
        if (users.isNotEmpty()) {
            LoginRegisterView(users[0], mainViewModel)
        } else {
            LoginRegisterView(null, mainViewModel)
        }
    }
}
//        HomeView()
//        AddEntryView()
//        ShowEntryView()
//        EditEntryView()
