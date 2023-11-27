package com.hn_2452.shoes_nike.di

import android.content.Context
import androidx.room.Room
import com.hn_2452.shoes_nike.data.local.NikeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDatabase(
        @ApplicationContext applicationContext: Context
    ): NikeDatabase {
        return Room
            .databaseBuilder(applicationContext, NikeDatabase::class.java, "nike-database")
            .build()
    }
}