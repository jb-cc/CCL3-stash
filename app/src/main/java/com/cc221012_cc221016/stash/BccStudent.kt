package com.cc221012_cc221016.stash

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "students")
data class BccStudent(
    val name: String,
    val uid: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)