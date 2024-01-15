package com.cc221012_cc221016.stash.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cc221012_cc221016.stash.BccStudent


@Database(entities = [BccStudent::class], version = 1)
abstract class StudentsDatabase: RoomDatabase() {
    abstract val dao: StudentDao
}