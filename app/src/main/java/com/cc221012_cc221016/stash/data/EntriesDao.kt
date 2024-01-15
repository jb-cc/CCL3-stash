package com.cc221012_cc221016.stash.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EntriesDao {
    @Insert
    suspend fun insertEntry(entry: Entries)

    @Update
    suspend fun updateEntry(entry: Entries)

    @Delete
    suspend fun deleteEntry(entry: Entries)

    @Query("SELECT * FROM entries")
    fun getEntries(): Flow<List<Entries>>
}