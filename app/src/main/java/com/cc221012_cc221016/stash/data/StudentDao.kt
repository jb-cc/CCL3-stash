package com.cc221012_cc221016.stash.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Insert
    suspend fun insertStudent(student: BccStudent)

    @Update
    suspend fun updateStudent(student: BccStudent)

    @Delete
    suspend fun deleteStudent(student: BccStudent)

    @Query("SELECT * FROM students")
    fun getStudents(): Flow<List<BccStudent>>
}