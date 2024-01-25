package com.cc221012_cc221016.stash.models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cc221012_cc221016.stash.data.Entries
import com.cc221012_cc221016.stash.data.EntriesDao
import com.cc221012_cc221016.stash.data.Users
import com.cc221012_cc221016.stash.data.UsersDao
import com.cc221012_cc221016.stash.ui.state.MainViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val entriesDao: EntriesDao, private val usersDao: UsersDao): ViewModel() {
    init {
        getEntries() // Fetch entries when ViewModel is created
    }

    //Entries
    private val _entriesState = MutableStateFlow(Entries("","", "", ""))
    val entriesState: StateFlow<Entries> = _entriesState.asStateFlow()

    //Users
    private val _usersState = MutableStateFlow(Users(""))
    val usersState: StateFlow<Users> = _usersState.asStateFlow()

    //MainViewState
    private val _mainViewState = MutableStateFlow(MainViewState())
    val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()

    private val _selectedEntry = MutableStateFlow<Entries?>(null)
    val selectedEntry = _selectedEntry.asStateFlow()

    private val _currentScreen = MutableStateFlow("Home")
    val currentScreen = _currentScreen.asStateFlow()

    fun logOut() {
        viewModelScope.launch {
            // Update the state to reflect that the user is not authenticated
            _mainViewState.update { currentState ->
                currentState.copy(isUserAuthenticated = false)
            }
            // Perform any additional cleanup or state updates necessary for logging out
        }
    }
    fun setCurrentScreen(screen: String) {
        _currentScreen.value = screen
    }

    fun setSelectedEntry(entry: Entries?) {
        _selectedEntry.value = entry
    }


    //ENTRIES METHODS

    //Save an entry

    suspend fun saveEntry(entry: Entries): Long {
        val id = entriesDao.insertEntry(entry)
        getEntries() // Update entries list
        return id // Return the generated ID
    }

    suspend fun getEntryById(id: Int): Entries {
        return entriesDao.getEntry(id.toLong()).first()
    }

    //Get all entries
    private fun getEntries() {
        viewModelScope.launch {
            entriesDao.getEntries().collect { allEntries ->
                Log.d("MainViewModel", "Fetched entries: $allEntries")
                _mainViewState.update { it.copy(entries = allEntries) }
            }
        }
    }


    //Update an entry
    fun updateEntry(entry: Entries) {
        viewModelScope.launch {
            entriesDao.updateEntry(entry)
            getEntries() // Refresh the list of entries
        }
    }



    //delete an entry

    fun deleteEntry(entry: Entries) {
        viewModelScope.launch {
            try {
                entriesDao.deleteEntry(entry)
                getEntries()
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error deleting entry: ${e.message}")
                // Handle the exception appropriately
            }
        }
    }




    //USER METHODS

    //Save a user
    fun saveUser(user: Users){
        viewModelScope.launch {
            usersDao.insertUser(user)
            _mainViewState.update { it.copy(isUserAuthenticated = true) }
        }
    }

    //Get a user
    private fun getUsers(){

        viewModelScope.launch {
            usersDao.getUsers().collect(){ allUsers ->
                _mainViewState.update { it.copy(users = allUsers) }
                Log.d("MainViewModel", "Fetched users: $allUsers")

            }
        }
    }

    fun authenticateUser() {
        _mainViewState.update { it.copy(isUserAuthenticated = true) }
    }

    fun initialGetUsers(): Flow<List<Users>> {
        return usersDao.getUsers()
    }
    fun deleteUser(user: Users){
        viewModelScope.launch {
            usersDao.deleteUser(user)
        }
        getUsers()
    }


}