package com.cc221012_cc221016.stash.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Entries::class], version = 1)
abstract class EntriesDatabase : RoomDatabase() {
    abstract val entriesDao: EntriesDao
}
