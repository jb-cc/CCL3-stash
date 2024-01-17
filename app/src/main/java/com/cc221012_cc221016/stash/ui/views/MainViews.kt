package com.cc221012_cc221016.stash.ui.views

// ============================ AJ ======================================


//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import com.cc221012_cc221016.stash.data.Entries
//import com.cc221012_cc221016.stash.models.MainViewModel
//import com.cc221012_cc221016.stash.ui.views.Composables.AddEntryView
//import com.cc221012_cc221016.stash.ui.views.Composables.HomeView
//import com.cc221012_cc221016.stash.ui.views.Composables.ShowEntryView
//
//
//sealed class Screen(val route: String){
//    object First: Screen("first")
//    object Second: Screen("second")
//}
//
//@Composable
//fun MainView(mainViewModel: MainViewModel) {
////        LoginRegisterView(hasUser = false)
////        LoginRegisterView(hasUser = true)
////    HomeView(mainViewModel) { selectedEntry -> ShowEntryView(selectedEntry) }
////  AddEntryView(mainViewModel)
////        ShowEntryView()
////        EditEntryView()
//
//    var currentScreen by remember { mutableStateOf("Home") }
//    var selectedEntry by remember { mutableStateOf<Entries?>(null) } // State to hold the selected entry
//
//
//    when (currentScreen) {
//        "Home" -> HomeView(mainViewModel) { entry ->
//            selectedEntry = entry
//            currentScreen = "ShowEntry"
//        }
//        "ShowEntry" -> selectedEntry?.let {
//            ShowEntryView(it) {
//                currentScreen = "Home" // This lambda sets the screen back to Home
//            }
//        }
//    }
//}


// =========================== JONAS ============================

//
//import android.util.Log
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import com.cc221012_cc221016.stash.models.MainViewModel
//import com.cc221012_cc221016.stash.ui.views.Composables.AddEntryView
//import com.cc221012_cc221016.stash.ui.views.Composables.EditEntryView
//import com.cc221012_cc221016.stash.ui.views.Composables.HomeView
//import com.cc221012_cc221016.stash.ui.views.Composables.LoginRegisterView
//import com.cc221012_cc221016.stash.ui.views.Composables.ShowEntryView
//
//
//
//sealed class Screen(val route: String){
//    object First: Screen("first")
//    object Second: Screen("second")
//}
//
//@Composable
//fun MainView(mainViewModel: MainViewModel){
//    val mainViewState by mainViewModel.mainViewState.collectAsState()
//    val users by mainViewModel.initialGetUsers().collectAsState(emptyList())
//    Log.d("MainView", "MainView: $mainViewState")
//    Log.d("MainView", "Got Users: $users")
//    if (mainViewState.isUserAuthenticated) {
//        HomeView()
//    } else {
//        if (users.isNotEmpty()) {
//            LoginRegisterView(users[0], mainViewModel)
//        } else {
//            LoginRegisterView(null, mainViewModel)
//        }
//    }
//}
////        HomeView()
////        AddEntryView()
////        ShowEntryView()
////        EditEntryView()
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cc221012_cc221016.stash.data.Entries
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
fun MainView(mainViewModel: MainViewModel) {
    val mainViewState by mainViewModel.mainViewState.collectAsState()
    val users by mainViewModel.initialGetUsers().collectAsState(emptyList())
    Log.d("MainView", "MainView: $mainViewState")
    Log.d("MainView", "Got Users: $users")

    var currentScreen by remember { mutableStateOf("Home") }
    var selectedEntry by remember { mutableStateOf<Entries?>(null) } // State to hold the selected entry

    if (mainViewState.isUserAuthenticated) {
        when (currentScreen) {
            "Home" -> HomeView(
                mainViewModel,
                navigateToShowEntry = { entry ->
                    selectedEntry = entry
                    currentScreen = "ShowEntry"
                },
                onAddEntryClick = {
                    currentScreen = "AddEntry"
                }
            )
            "ShowEntry" -> selectedEntry?.let { entry ->
                ShowEntryView(entry, onBack = {
                    currentScreen = "Home"
                }, onDeleteEntry = { entryToDelete ->
                    mainViewModel.deleteEntry(entryToDelete)
                    currentScreen = "Home"
                }, onEditEntry = { entryToEdit ->
                    selectedEntry = entryToEdit
                    currentScreen = "EditEntry"
                })
            }
            "AddEntry" -> AddEntryView(mainViewModel, onBack = {
                currentScreen = "Home"
            }, navigateToShowEntry = { entry ->
                selectedEntry = entry
                currentScreen = "ShowEntry"
            })
            "EditEntry" -> selectedEntry?.let {
                EditEntryView(it, onBack = {
                    currentScreen = "ShowEntry"
                }, onSave = { updatedEntry ->
                    mainViewModel.updateEntry(updatedEntry) // Update entry in the database
                    selectedEntry = updatedEntry // Update the selected entry with new details
                    currentScreen = "ShowEntry" // Navigate back to the ShowEntryView with updated entry
                })
            }
        }
    } else {
        if (users.isNotEmpty()) {
            LoginRegisterView(users[0], mainViewModel)
        } else {
            LoginRegisterView(null, mainViewModel)
        }
    }
}
