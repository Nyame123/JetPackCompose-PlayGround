package com.bismark.core_data.data.testdoubles

import com.bismark.core.database.dao.TopicDao
import com.bismark.core.database.model.TopicEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * Test double for [TopicDao]
 */
class TestTopicDao : TopicDao {

    private var entitiesStateFlow = MutableStateFlow(
        listOf(
            TopicEntity(
                id = "1",
                name = "Topic",
                shortDescription = "short description",
                longDescription = "long description",
                url = "URL",
                imageUrl = "image URL",
            )
        )
    )

    override fun getTopicEntity(topicId: String): Flow<TopicEntity> {
        throw NotImplementedError("Unused in tests")
    }

    override fun getTopicEntitiesStream(): Flow<List<TopicEntity>> =
        entitiesStateFlow

    override fun getTopicEntitiesStream(ids: Set<String>): Flow<List<TopicEntity>> =
        getTopicEntitiesStream()
            .map { topics -> topics.filter { it.id in ids } }

    override suspend fun insertOrIgnoreTopics(topicEntities: List<TopicEntity>): List<Long> {
        entitiesStateFlow.value = topicEntities
        // Assume no conflicts on insert
        return topicEntities.map { it.id.toLong() }
    }

    override suspend fun updateTopics(entities: List<TopicEntity>) {
        throw NotImplementedError("Unused in tests")
    }

    override suspend fun deleteTopics(ids: List<String>) {
        val idSet = ids.toSet()
        entitiesStateFlow.update { entities ->
            entities.filterNot { idSet.contains(it.id) }
        }
    }
}
