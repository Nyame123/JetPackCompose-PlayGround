package com.bismark.core.testing.repository

import com.bismark.core.model.Author
import com.bismark.core_data.Synchronizer
import com.bismark.core_data.repository.AuthorsRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestAuthorsRepository : AuthorsRepository {
    /**
     * The backing hot flow for the list of followed author ids for testing.
     */
    private val _followedAuthorIds: MutableSharedFlow<Set<String>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * The backing hot flow for the list of author ids for testing.
     */
    private val authorsFlow: MutableSharedFlow<List<Author>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getAuthorsStream(): Flow<List<Author>> = authorsFlow

    override fun getAuthorStream(id: String): Flow<Author> {
        return authorsFlow.map { authors -> authors.find { it.id == id }!! }
    }

    override fun getFollowedAuthorIdsStream(): Flow<Set<String>> = _followedAuthorIds

    override suspend fun setFollowedAuthorIds(followedAuthorIds: Set<String>) {
        _followedAuthorIds.tryEmit(followedAuthorIds)
    }

    override suspend fun toggleFollowedAuthorId(followedAuthorId: String, followed: Boolean) {
        getCurrentFollowedAuthors()?.let { current ->
            _followedAuthorIds.tryEmit(
                if (followed) current.plus(followedAuthorId)
                else current.minus(followedAuthorId)
            )
        }
    }

    override suspend fun syncWith(synchronizer: Synchronizer) = true

    /**
     * A test-only API to allow controlling the list of authors from tests.
     */
    fun sendAuthors(authors: List<Author>) {
        authorsFlow.tryEmit(authors)
    }

    /**
     * A test-only API to allow querying the current followed authors.
     */
    fun getCurrentFollowedAuthors(): Set<String>? = _followedAuthorIds.replayCache.firstOrNull()
}
