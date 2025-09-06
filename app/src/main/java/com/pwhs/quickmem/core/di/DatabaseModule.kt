package com.pwhs.quickmem.core.di

import android.content.Context
import androidx.room.Room
import com.pwhs.quickmem.data.local.AppDatabase
import com.pwhs.quickmem.data.local.dao.SearchQueryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideSearchDao(database: AppDatabase): SearchQueryDao {
        return database.searchQueryDao()
    }
}