package com.bismark.core.database

import com.bismark.core.database.dao.AuthorDao
import com.bismark.core.database.dao.EpisodeDao
import com.bismark.core.database.dao.NewsResourceDao
import com.bismark.core.database.dao.TopicDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesAuthorDao(
        database: JPGDatabase,
    ): AuthorDao = database.authorDao()

    @Provides
    fun providesTopicsDao(
        database: JPGDatabase,
    ): TopicDao = database.topicDao()

    @Provides
    fun providesEpisodeDao(
        database: JPGDatabase,
    ): EpisodeDao = database.episodeDao()

    @Provides
    fun providesNewsResourceDao(
        database: JPGDatabase,
    ): NewsResourceDao = database.newsResourceDao()
}
