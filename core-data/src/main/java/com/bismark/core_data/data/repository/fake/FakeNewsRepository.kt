package com.bismark.core_data.data.repository.fake

import com.bismark.core.common.network.di.Dispatcher
import com.bismark.core.common.network.di.JPGDispatchers
import com.bismark.core.database.model.NewsResourceEntity
import com.bismark.core.database.model.asExternalModel
import com.bismark.core.model.NewsResource
import com.bismark.core_data.Synchronizer
import com.bismark.core_data.model.asEntity
import com.bismark.core_data.data.repository.NewsRepository
import com.bismark.core_network.fake.FakeDataSource
import com.bismark.core_network.model.NetworkNewsResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Fake implementation of the [NewsRepository] that retrieves the news resources from a JSON String.
 *
 * This allows us to run the app with fake data, without needing an internet connection or working
 * backend.
 */
class FakeNewsRepository @Inject constructor(
    @Dispatcher(JPGDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json
) : NewsRepository {

    override fun getNewsResourcesStream(): Flow<List<NewsResource>> =
        flow {
            emit(
                networkJson.decodeFromString<List<NetworkNewsResource>>(FakeDataSource.data)
                    .map(NetworkNewsResource::asEntity)
                    .map(NewsResourceEntity::asExternalModel)
            )
        }
            .flowOn(ioDispatcher)

    override fun getNewsResourcesStream(
        filterAuthorIds: Set<String>,
        filterTopicIds: Set<String>,
    ): Flow<List<NewsResource>> =
        flow {
            emit(
                networkJson.decodeFromString<List<NetworkNewsResource>>(FakeDataSource.data)
                    .filter {
                        it.authors.intersect(filterAuthorIds).isNotEmpty() ||
                                it.topics.intersect(filterTopicIds).isNotEmpty()
                    }
                    .map(NetworkNewsResource::asEntity)
                    .map(NewsResourceEntity::asExternalModel)
            )
        }
            .flowOn(ioDispatcher)

    override suspend fun syncWith(synchronizer: Synchronizer) = true
}
