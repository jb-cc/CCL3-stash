package com.cc221012_cc221016.stash.ui.views


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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

    val currentScreen by mainViewModel.currentScreen.observeAsState("Home")
    val selectedEntry by mainViewModel.selectedEntry.observeAsState()

    if (mainViewState.isUserAuthenticated) {
        when (currentScreen) {
            "Home" -> HomeView(
                mainViewModel = mainViewModel,
                navigateToShowEntry = { entry ->
                    mainViewModel.setSelectedEntry(entry)
                    mainViewModel.setCurrentScreen("ShowEntry")
                },
                onAddEntryClick = {
                    mainViewModel.setCurrentScreen("AddEntry")
                }
            )
            "ShowEntry" -> selectedEntry?.let { entry ->
                ShowEntryView(
                    entryId = entry.entryID,
                    onBack = { mainViewModel.setCurrentScreen("Home") },
                    onDeleteEntry = { entryToDelete ->
                        mainViewModel.deleteEntry(entryToDelete)
                        mainViewModel.setCurrentScreen("Home")
                    },
                    onEditEntry = { entryToEdit ->
                        mainViewModel.setSelectedEntry(entryToEdit)
                        mainViewModel.setCurrentScreen("EditEntry")
                    },
                    mainViewModel = mainViewModel
                )
            }
            "AddEntry" -> AddEntryView(
                mainViewModel = mainViewModel,
                onBack = { mainViewModel.setCurrentScreen("Home") },
                navigateToShowEntry = { entry ->
                    mainViewModel.setSelectedEntry(entry)
                    mainViewModel.setCurrentScreen("ShowEntry")
                }
            )
            "EditEntry" -> selectedEntry?.let { entry ->
                EditEntryView(
                    entry = entry,
                    onBack = { mainViewModel.setCurrentScreen("ShowEntry") },
                    onSave = { updatedEntry ->
                        mainViewModel.updateEntry(updatedEntry)
                        mainViewModel.setSelectedEntry(updatedEntry)
                        mainViewModel.setCurrentScreen("ShowEntry")
                    }
                )
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
