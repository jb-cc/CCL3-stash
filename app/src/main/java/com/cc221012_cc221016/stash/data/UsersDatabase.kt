package com.cc221012_cc221016.stash.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Users::class], version = 1)
abstract class UsersDatabase : RoomDatabase() {
    abstract val usersDao: UsersDao
}
