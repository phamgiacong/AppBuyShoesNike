package com.hn_2452.shoes_nike.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hn_2452.shoes_nike.data.model.Searching
import com.hn_2452.shoes_nike.data.model.User

@Database(entities = [Searching::class, User::class], version = 1)
@TypeConverters(Converters::class)
abstract class NikeDatabase : RoomDatabase() {
    abstract fun searchingDao(): SearchingDao
    abstract fun userDao() : UserDao
}

class Converters {
    @TypeConverter
    fun stringListToString(strings: List<String>): String {
        return Gson().toJson(strings)
    }

    @TypeConverter
    fun stringToStringList(string: String): List<String> {
        return Gson().fromJson(string, genericType<List<String>>())
    }
}

inline fun <reified T> genericType() = object: TypeToken<T>() {}.type