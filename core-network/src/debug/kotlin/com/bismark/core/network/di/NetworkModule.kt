package com.bismark.core.network.di

import com.bismark.core_network.JPGNetwork
import com.bismark.core_network.fake.FakeJPGNetwork
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun bindsNiANetwork(
        niANetwork: FakeJPGNetwork
    ): JPGNetwork

    companion object {

        @Provides
        @Singleton
        fun providesNetworkJson(): Json = Json {
            ignoreUnknownKeys = true
        }
    }
}
