package com.cc221012_cc221016.stash

import android.content.Context
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
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory


class MainActivity : ComponentActivity() {
    //factory for sqlcipher
    private val factory by lazy {
        // Retrieve the encrypted passphrase from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val encryptedPassphrase = sharedPreferences.getString("encryptedPassphraseKey", "")

        // Decrypt the passphrase
        val decryptedPassphrase = encryptedPassphrase?.let { KeystoreHelper.decryptData(it) }

        // Convert passphrase to ByteArray and initialize SupportFactory
        val passphrase = SQLiteDatabase.getBytes(decryptedPassphrase?.toCharArray())
        SupportFactory(passphrase)
    }

    // Lazy initialization of EntriesDatabase
    private val entriesDB by lazy {
        Room.databaseBuilder(this, EntriesDatabase::class.java, "EntriesDatabase.db")
            .openHelperFactory(factory)
            .build()
    }

    // Lazy initialization of UsersDatabase
    private val usersDB by lazy {
        Room.databaseBuilder(this, UsersDatabase::class.java, "UsersDatabase.db")
            .openHelperFactory(factory)
            .build()
    }

    //initialize Passphrase if not set
    private fun initializePassphraseIfNeeded() {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        if (!sharedPreferences.contains("encryptedPassphraseKey")) {
            val encryptedPassphrase = KeystoreHelper.encryptData("YourActualPassphrase")
            sharedPreferences.edit().putString("encryptedPassphraseKey", encryptedPassphrase)
                .apply()
        }
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
        SQLiteDatabase.loadLibs(this) // Load SQLCipher libraries
        KeystoreHelper.generateKey() // Ensure the key is generated
        // Initialize passphrase at the start
        initializePassphraseIfNeeded()
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

