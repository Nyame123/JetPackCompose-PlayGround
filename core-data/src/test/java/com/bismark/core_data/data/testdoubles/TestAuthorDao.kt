package com.bismark.core_data.data.testdoubles

import com.bismark.core.database.dao.AuthorDao
import com.bismark.core.database.model.AuthorEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Test double for [AuthorDao]
 */
class TestAuthorDao : AuthorDao {

    private var entitiesStateFlow = MutableStateFlow(
        listOf(
            AuthorEntity(
                id = "1",
                name = "Topic",
                imageUrl = "imageUrl",
                twitter = "twitter",
                mediumPage = "mediumPage",
                bio = "bio",
            )
        )
    )

    override fun getAuthorEntitiesStream(): Flow<List<AuthorEntity>> =
        entitiesStateFlow

    override fun getAuthorEntityStream(authorId: String): Flow<AuthorEntity> {
        throw NotImplementedError("Unused in tests")
    }

    override suspend fun insertOrIgnoreAuthors(authorEntities: List<AuthorEntity>): List<Long> {
        entitiesStateFlow.value = authorEntities
        // Assume no conflicts on insert
        return authorEntities.map { it.id.toLong() }
    }

    override suspend fun updateAuthors(entities: List<AuthorEntity>) {
        throw NotImplementedError("Unused in tests")
    }

    override suspend fun deleteAuthors(ids: List<String>) {
        val idSet = ids.toSet()
        entitiesStateFlow.update { entities ->
            entities.filterNot { idSet.contains(it.id) }
        }
    }
}
