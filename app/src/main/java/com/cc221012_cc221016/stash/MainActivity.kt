package com.cc221012_cc221016.stash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.cc221012_cc221016.stash.data.EntriesDatabase
import com.cc221012_cc221016.stash.data.UsersDatabase
import com.cc221012_cc221016.stash.ui.views.MainView
import com.cc221012_cc221016.stash.models.MainViewModel
import com.cc221012_cc221016.stash.ui.theme.stashTheme

class MainActivity : ComponentActivity() {
    // Lazy initialization of EntriesDatabase
    private val entriesDB by lazy {
        Room.databaseBuilder(this, EntriesDatabase::class.java, "EntriesDatabase.db").build()
    }

    // Lazy initialization of UsersDatabase
    private val usersDB by lazy {
        Room.databaseBuilder(this, UsersDatabase::class.java, "UsersDatabase.db").build()
    }

    private val mainViewModel by viewModels<MainViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    // Providing both DAOs to the MainViewModel
                    return MainViewModel(entriesDB.entriesDao, usersDB.usersDao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            stashTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(mainViewModel)
                }
            }
        }
    }
}

