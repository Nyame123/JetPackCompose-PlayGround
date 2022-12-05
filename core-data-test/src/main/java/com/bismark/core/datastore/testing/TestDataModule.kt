package com.bismark.core.datastore.testing

import com.bismark.core_data.data.repository.AuthorsRepository
import com.bismark.core_data.data.repository.NewsRepository
import com.bismark.core_data.data.repository.TopicsRepository
import com.bismark.core_data.data.repository.fake.FakeAuthorsRepository
import com.bismark.core_data.data.repository.fake.FakeNewsRepository
import com.bismark.core_data.data.repository.fake.FakeTopicsRepository
import com.bismark.core_data.di.DataModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
interface TestDataModule {

    @Binds
    fun bindsTopicRepository(
        fakeTopicsRepository: FakeTopicsRepository
    ): TopicsRepository

    @Binds
    fun bindsAuthorRepository(
        fakeAuthorsRepository: FakeAuthorsRepository
    ): AuthorsRepository

    @Binds
    fun bindsNewsResourceRepository(
        fakeNewsRepository: FakeNewsRepository
    ): NewsRepository
}
