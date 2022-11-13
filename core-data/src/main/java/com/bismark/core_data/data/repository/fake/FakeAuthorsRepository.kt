package com.bismark.core_data.data.repository.fake

import com.bismark.core.common.network.di.Dispatcher
import com.bismark.core.common.network.di.JPGDispatchers
import com.bismark.core.model.Author
import com.bismark.core_data.Synchronizer
import com.bismark.core_data.data.repository.AuthorsRepository
import com.bismark.core_datastore.JPGPreferences
import com.bismark.core_network.fake.FakeDataSource
import com.bismark.core_network.model.NetworkAuthor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Fake implementation of the [AuthorsRepository] that returns hardcoded authors.
 *
 * This allows us to run the app with fake data, without needing an internet connection or working
 * backend.
 */
class FakeAuthorsRepository @Inject constructor(
    @Dispatcher(JPGDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val niaPreferences: JPGPreferences,
    private val networkJson: Json,
) : AuthorsRepository {

    override fun getAuthorsStream(): Flow<List<Author>> = flow {
        emit(
            networkJson.decodeFromString<List<NetworkAuthor>>(FakeDataSource.authors).map {
                Author(
                    id = it.id,
                    name = it.name,
                    imageUrl = it.imageUrl,
                    twitter = it.twitter,
                    mediumPage = it.mediumPage,
                    bio = it.bio,
                )
            }
        )
    }
        .flowOn(ioDispatcher)

    override fun getAuthorStream(id: String): Flow<Author> {
        return getAuthorsStream().map { it.first { author -> author.id == id } }
    }

    override suspend fun setFollowedAuthorIds(followedAuthorIds: Set<String>) {
        niaPreferences.setFollowedAuthorIds(followedAuthorIds)
    }

    override suspend fun toggleFollowedAuthorId(followedAuthorId: String, followed: Boolean) {
        niaPreferences.toggleFollowedAuthorId(followedAuthorId, followed)
    }

    override fun getFollowedAuthorIdsStream(): Flow<Set<String>> = niaPreferences.followedAuthorIds

    override suspend fun syncWith(synchronizer: Synchronizer) = true
}
