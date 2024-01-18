package com.cc221012_cc221016.stash.ui.views


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cc221012_cc221016.stash.data.Entries
import com.cc221012_cc221016.stash.models.MainViewModel
import com.cc221012_cc221016.stash.ui.views.composables.AddEntryView
import com.cc221012_cc221016.stash.ui.views.composables.EditEntryView
import com.cc221012_cc221016.stash.ui.views.composables.HomeView
import com.cc221012_cc221016.stash.ui.views.composables.LoginRegisterView
import com.cc221012_cc221016.stash.ui.views.composables.ShowEntryView

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
                ShowEntryView(
                    entryId = entry.entryID.toLong(),
                    onBack = {
                        currentScreen = "Home"
                    },
                    onDeleteEntry = { entryToDelete ->
                        mainViewModel.deleteEntry(entryToDelete)
                        currentScreen = "Home"
                    },
                    onEditEntry = { entryToEdit ->
                        selectedEntry = entryToEdit
                        currentScreen = "EditEntry"
                    },
                    mainViewModel = mainViewModel // Add this line
                )
            }

            "AddEntry" -> AddEntryView(mainViewModel, onBack = {
                currentScreen = "Home"
            }, navigateToShowEntry = { entry ->
                selectedEntry = entry
                currentScreen = "ShowEntry"
            })
            "EditEntry" -> selectedEntry?.let { entry ->
            EditEntryView(entry, onBack = {
                currentScreen = "ShowEntry"
            }, onSave = { updatedEntry ->
                mainViewModel.updateEntry(updatedEntry)
                selectedEntry = updatedEntry // Update the selected entry
                currentScreen = "ShowEntry"
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
