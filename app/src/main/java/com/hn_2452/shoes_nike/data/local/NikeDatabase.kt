package com.hn_2452.shoes_nike.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hn_2452.shoes_nike.data.model.Searching

@Database(entities = [Searching::class], version = 1)
abstract class NikeDatabase : RoomDatabase() {
    abstract fun searchingDao(): SearchingDao
}