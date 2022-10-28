package com.bismark.core_data.di

import com.bismark.core_data.repository.AuthorsRepository
import com.bismark.core_data.repository.NewsRepository
import com.bismark.core_data.repository.OfflineFirstAuthorsRepository
import com.bismark.core_data.repository.OfflineFirstNewsRepository
import com.bismark.core_data.repository.OfflineFirstTopicsRepository
import com.bismark.core_data.repository.TopicsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsTopicRepository(
        topicsRepository: OfflineFirstTopicsRepository
    ): TopicsRepository

    @Binds
    fun bindsAuthorsRepository(
        authorsRepository: OfflineFirstAuthorsRepository
    ): AuthorsRepository

    @Binds
    fun bindsNewsResourceRepository(
        newsRepository: OfflineFirstNewsRepository
    ): NewsRepository
}
