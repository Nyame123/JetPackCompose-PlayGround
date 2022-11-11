package com.bismark.core.testing.di

import com.bismark.core.common.network.DispatchersModule
import com.bismark.core.common.network.di.Dispatcher
import com.bismark.core.common.network.di.JPGDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatchersModule::class],
)
object TestDispatchersModule {
    @Provides
    @Dispatcher(JPGDispatchers.IO)
    fun providesIODispatcher(testDispatcher: TestDispatcher): CoroutineDispatcher = testDispatcher
}
