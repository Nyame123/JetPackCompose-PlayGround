package com.bismark.core_data.data.repository.fake

import com.bismark.core.common.network.di.Dispatcher
import com.bismark.core.common.network.di.JPGDispatchers
import com.bismark.core.model.Topic
import com.bismark.core_data.Synchronizer
import com.bismark.core_data.data.repository.TopicsRepository
import com.bismark.core_datastore.JPGPreferences
import com.bismark.core_network.fake.FakeDataSource
import com.bismark.core_network.model.NetworkTopic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Fake implementation of the [TopicsRepository] that retrieves the topics from a JSON String, and
 * uses a local DataStore instance to save and retrieve followed topic ids.
 *
 * This allows us to run the app with fake data, without needing an internet connection or working
 * backend.
 */
class FakeTopicsRepository @Inject constructor(
    @Dispatcher(JPGDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val niaPreferences: JPGPreferences
) : TopicsRepository {

    override fun getTopicsStream(): Flow<List<Topic>> = flow<List<Topic>> {
        emit(
            networkJson.decodeFromString<List<NetworkTopic>>(FakeDataSource.topicsData).map {
                Topic(
                    id = it.id,
                    name = it.name,
                    shortDescription = it.shortDescription,
                    longDescription = it.longDescription,
                    url = it.url,
                    imageUrl = it.imageUrl
                )
            }
        )
    }
        .flowOn(ioDispatcher)

    override fun getTopic(id: String): Flow<Topic> {
        return getTopicsStream().map { it.first { topic -> topic.id == id } }
    }

    override suspend fun setFollowedTopicIds(followedTopicIds: Set<String>) =
        niaPreferences.setFollowedTopicIds(followedTopicIds)

    override suspend fun toggleFollowedTopicId(followedTopicId: String, followed: Boolean) =
        niaPreferences.toggleFollowedTopicId(followedTopicId, followed)

    override fun getFollowedTopicIdsStream() = niaPreferences.followedTopicIds

    override suspend fun syncWith(synchronizer: Synchronizer) = true
}
