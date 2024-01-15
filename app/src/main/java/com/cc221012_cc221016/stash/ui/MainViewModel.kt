package com.cc221012_cc221016.stash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cc221012_cc221016.stash.data.Entries
import com.cc221012_cc221016.stash.data.EntriesDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val dao: EntriesDao): ViewModel() {
    private val _entriesState = MutableStateFlow(Entries("",""))
    val entriesState: StateFlow<Entries> = _entriesState.asStateFlow()
    private val _mainViewState = MutableStateFlow(MainViewState())
    val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()

    fun save(student: Entries){
        viewModelScope.launch {
            dao.insertStudent(student)
        }
    }

    fun getStudents(){
        viewModelScope.launch {
            dao.getStudents().collect(){ allStudents ->
                _mainViewState.update { it.copy(students = allStudents) }
            }
        }
    }

    fun editStudent(student: Entries){
        _entriesState.value = student
        _mainViewState.update { it.copy(openDialog = true) }
    }

    fun updateStudent(student: Entries){
        viewModelScope.launch {
            dao.updateStudent(student)
        }
        getStudents()
        closeDialog()
    }

    fun closeDialog(){
        _mainViewState.update { it.copy(openDialog = false) }
    }

    fun deleteButton(student: Entries){
        viewModelScope.launch {
            dao.deleteStudent(student)
        }
        getStudents()
    }

    fun selectScreen(screen: Screen){
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }
}