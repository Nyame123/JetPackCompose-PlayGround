package com.bismark.core.testing.repository

import com.bismark.core.model.Author
import com.bismark.core.model.NewsResource
import com.bismark.core.model.Topic
import com.bismark.core_data.Synchronizer
import com.bismark.core_data.data.repository.NewsRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestNewsRepository : NewsRepository {

    /**
     * The backing hot flow for the list of topics ids for testing.
     */
    private val newsResourcesFlow: MutableSharedFlow<List<NewsResource>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getNewsResourcesStream(): Flow<List<NewsResource>> = newsResourcesFlow

    override fun getNewsResourcesStream(
        filterAuthorIds: Set<String>,
        filterTopicIds: Set<String>
    ): Flow<List<NewsResource>> =
        getNewsResourcesStream().map { newsResources ->
            newsResources
                .filter {
                    it.authors.map(Author::id).intersect(filterAuthorIds).isNotEmpty() ||
                        it.topics.map(Topic::id).intersect(filterTopicIds).isNotEmpty()
                }
        }

    /**
     * A test-only API to allow controlling the list of news resources from tests.
     */
    fun sendNewsResources(newsResources: List<NewsResource>) {
        newsResourcesFlow.tryEmit(newsResources)
    }

    override suspend fun syncWith(synchronizer: Synchronizer) = true
}
