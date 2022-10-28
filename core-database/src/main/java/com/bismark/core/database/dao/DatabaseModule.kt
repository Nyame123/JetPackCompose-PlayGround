package com.bismark.core.database.dao

import android.content.Context
import androidx.room.Room
import com.bismark.core.database.JPGDatabase
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
    fun providesNiADatabase(
        @ApplicationContext context: Context,
    ): JPGDatabase = Room.databaseBuilder(
        context,
        JPGDatabase::class.java,
        "nia-database"
    ).build()
}
