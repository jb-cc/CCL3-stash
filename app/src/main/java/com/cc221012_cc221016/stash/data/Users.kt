package com.cc221012_cc221016.stash.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "users")
data class Users(
    val userPassword: String,
    @PrimaryKey(autoGenerate = true) val userID: Int = 0
)