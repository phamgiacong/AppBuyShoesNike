package com.hn_2452.shoes_nike.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hn_2452.shoes_nike.data.model.Searching

@Dao
interface SearchingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSearching(searching: Searching)

    @Delete
    suspend fun deleteSearching(searching: Searching)

    @Query("DELETE FROM searching")
    suspend fun deleteAllSearch()

    @Query("SELECT * FROM searching")
    fun getAllSearching() : LiveData<List<Searching>>

}