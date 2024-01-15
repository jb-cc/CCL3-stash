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
import com.cc221012_cc221016.stash.ui.MainView
import com.cc221012_cc221016.stash.ui.MainViewModel
import com.cc221012_cc221016.stash.ui.theme.stashTheme

class MainActivity : ComponentActivity() {
    private val db by lazy{
        Room.databaseBuilder(this, EntriesDatabase::class.java, "StudentsDatabase.db").build()
    }

    // Do not initialize it as:
    // private val mainViewModel = MainViewModel(db.dao)
    // With the delegate/lazy initialization, it will be called after onCreate
    // https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories

    private val mainViewModel by viewModels<MainViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T{
                    return MainViewModel(db.dao) as T
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
