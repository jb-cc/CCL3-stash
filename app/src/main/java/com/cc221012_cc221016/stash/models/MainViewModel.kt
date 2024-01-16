package com.cc221012_cc221016.stash.models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cc221012_cc221016.stash.data.Entries
import com.cc221012_cc221016.stash.data.EntriesDao
import com.cc221012_cc221016.stash.data.UsersDao
import com.cc221012_cc221016.stash.data.EntriesDatabase
import com.cc221012_cc221016.stash.data.Users
import com.cc221012_cc221016.stash.data.UsersDatabase
import com.cc221012_cc221016.stash.ui.views.Screen
import com.cc221012_cc221016.stash.ui.state.MainViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val entriesDao: EntriesDao, private val usersDao: UsersDao): ViewModel() {
    //Entries
    private val _entriesState = MutableStateFlow(Entries("","", "", ""))
    val entriesState: StateFlow<Entries> = _entriesState.asStateFlow()

    //Users
    private val _usersState = MutableStateFlow(Users(""))
    val usersState: StateFlow<Users> = _usersState.asStateFlow()

    //MainViewState
    private val _mainViewState = MutableStateFlow(MainViewState())
    val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()



    //ENTRIES METHODS

    //Save an entry
    fun saveEntry(entry: Entries){
        viewModelScope.launch {
            entriesDao.insertEntry(entry)
        }
    }

    //Get all entries
    fun getEntries(){
        viewModelScope.launch {
            entriesDao.getEntries().collect(){ allEntries ->
                _mainViewState.update { it.copy(entries = allEntries) }
            }
        }
    }

    //Get an entry
    fun getEntry(id: Int){
        viewModelScope.launch {
            entriesDao.getEntry(id).collect(){ entry ->
                _entriesState.update { entry }
            }
        }
    }

    //Update an entry
    fun updateEntry(entry: Entries){
        viewModelScope.launch {
            entriesDao.updateEntry(entry)
        }
        getEntries()
        closeDialog()
    }

    //edit an entry

    fun editEntry(entry: Entries){
        _entriesState.value = entry
        _mainViewState.update { it.copy(openDialog = true) }
    }

    //delete an entry

    fun deleteEntry(entry: Entries){
        viewModelScope.launch {
            entriesDao.deleteEntry(entry)
        }
        getEntries()
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
    fun getUsers(){

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




    fun selectScreen(screen: Screen){
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }

    fun closeDialog(){
        _mainViewState.update { it.copy(openDialog = false) }
    }
}