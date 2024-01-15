package com.cc221012_cc221016.stash.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "entries")
data class Entries(
    val entryName: String,
    val entryUsername: String,
    val entryPassword: String,
    val entryUrl: String,
    @PrimaryKey(autoGenerate = true) val entryID: Int = 0
)