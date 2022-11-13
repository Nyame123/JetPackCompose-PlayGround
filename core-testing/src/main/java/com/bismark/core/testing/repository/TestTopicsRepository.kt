package com.bismark.core.testing.repository

import com.bismark.core.model.Topic
import com.bismark.core_data.Synchronizer
import com.bismark.core_data.data.repository.TopicsRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestTopicsRepository : TopicsRepository {

    /**
     * The backing hot flow for the list of followed topic ids for testing.
     */
    private val _followedTopicIds: MutableSharedFlow<Set<String>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * The backing hot flow for the list of topics ids for testing.
     */
    private val topicsFlow: MutableSharedFlow<List<Topic>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getTopicsStream(): Flow<List<Topic>> = topicsFlow

    override fun getTopic(id: String): Flow<Topic> {
        return topicsFlow.map { topics -> topics.find { it.id == id }!! }
    }

    override suspend fun setFollowedTopicIds(followedTopicIds: Set<String>) {
        _followedTopicIds.tryEmit(followedTopicIds)
    }

    override suspend fun toggleFollowedTopicId(followedTopicId: String, followed: Boolean) {
        getCurrentFollowedTopics()?.let { current ->
            _followedTopicIds.tryEmit(
                if (followed) current.plus(followedTopicId)
                else current.minus(followedTopicId)
            )
        }
    }

    override fun getFollowedTopicIdsStream(): Flow<Set<String>> = _followedTopicIds

    /**
     * A test-only API to allow controlling the list of topics from tests.
     */
    fun sendTopics(topics: List<Topic>) {
        topicsFlow.tryEmit(topics)
    }

    /**
     * A test-only API to allow querying the current followed topics.
     */
    fun getCurrentFollowedTopics(): Set<String>? = _followedTopicIds.replayCache.firstOrNull()

    override suspend fun syncWith(synchronizer: Synchronizer) = true
}
