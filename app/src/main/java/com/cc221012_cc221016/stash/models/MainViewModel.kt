package com.cc221012_cc221016.stash.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cc221012_cc221016.stash.data.BccStudent
import com.cc221012_cc221016.stash.data.StudentDao
import com.cc221012_cc221016.stash.ui.views.Screen
import com.cc221012_cc221016.stash.ui.state.MainViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val studentDao: StudentDao): ViewModel() {
    private val _bccStudentState = MutableStateFlow(BccStudent("",""))
    val bccStudentState: StateFlow<BccStudent> = _bccStudentState.asStateFlow()
    private val _mainViewState = MutableStateFlow(MainViewState())
    val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()

    fun save(student: BccStudent){
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

    fun editStudent(student: BccStudent){
        _bccStudentState.value = student
        _mainViewState.update { it.copy(openDialog = true) }
    }

    fun updateStudent(student: BccStudent){
        viewModelScope.launch {
            dao.updateStudent(student)
        }
        getStudents()
        closeDialog()
    }

    fun closeDialog(){
        _mainViewState.update { it.copy(openDialog = false) }
    }

    fun deleteButton(student: BccStudent){
        viewModelScope.launch {
            dao.deleteStudent(student)
        }
        getStudents()
    }

    fun selectScreen(screen: Screen){
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }
}