package com.cc221012_cc221016.stash.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cc221012_cc221016.stash.data.Entries
import com.cc221012_cc221016.stash.models.MainViewModel
import com.cc221012_cc221016.stash.ui.views.Composables.AddEntryView
import com.cc221012_cc221016.stash.ui.views.Composables.HomeView
import com.cc221012_cc221016.stash.ui.views.Composables.ShowEntryView


sealed class Screen(val route: String){
    object First: Screen("first")
    object Second: Screen("second")
}

@Composable
fun MainView(mainViewModel: MainViewModel) {
//        LoginRegisterView(hasUser = false)
//        LoginRegisterView(hasUser = true)
//    HomeView(mainViewModel) { selectedEntry -> ShowEntryView(selectedEntry) }
//  AddEntryView(mainViewModel)
//        ShowEntryView()
//        EditEntryView()

    var currentScreen by remember { mutableStateOf("Home") }
    var selectedEntry by remember { mutableStateOf<Entries?>(null) } // State to hold the selected entry


    when (currentScreen) {
        "Home" -> HomeView(mainViewModel) { entry ->
            selectedEntry = entry
            currentScreen = "ShowEntry"
        }
        "ShowEntry" -> selectedEntry?.let {
            ShowEntryView(it) {
                currentScreen = "Home" // This lambda sets the screen back to Home
            }
        }
    }
}
