package com.bismark.core_data.data.repository

import com.bismark.core.database.dao.TopicDao
import com.bismark.core.database.model.TopicEntity
import com.bismark.core.database.model.asExternalModel
import com.bismark.core.model.Topic
import com.bismark.core_data.Synchronizer
import com.bismark.core_data.changeListSync
import com.bismark.core_data.model.asEntity
import com.bismark.core_datastore.ChangeListVersions
import com.bismark.core_datastore.JPGPreferences
import com.bismark.core_network.JPGNetwork
import com.bismark.core_network.model.NetworkTopic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Disk storage backed implementation of the [TopicsRepository].
 * Reads are exclusively from local storage to support offline access.
 */
class OfflineFirstTopicsRepository @Inject constructor(
    private val topicDao: TopicDao,
    private val network: JPGNetwork,
    private val niaPreferences: JPGPreferences,
) : TopicsRepository {

    override fun getTopicsStream(): Flow<List<Topic>> =
        topicDao.getTopicEntitiesStream()
            .map { it.map(TopicEntity::asExternalModel) }

    override fun getTopic(id: String): Flow<Topic> =
        topicDao.getTopicEntity(id).map { it.asExternalModel() }

    override suspend fun setFollowedTopicIds(followedTopicIds: Set<String>) =
        niaPreferences.setFollowedTopicIds(followedTopicIds)

    override suspend fun toggleFollowedTopicId(followedTopicId: String, followed: Boolean) =
        niaPreferences.toggleFollowedTopicId(followedTopicId, followed)

    override fun getFollowedTopicIdsStream() = niaPreferences.followedTopicIds

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::topicVersion,
            changeListFetcher = { currentVersion ->
                network.getTopicChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(topicVersion = latestVersion)
            },
            modelDeleter = topicDao::deleteTopics,
            modelUpdater = { changedIds ->
                val networkTopics = network.getTopics(ids = changedIds)
                topicDao.upsertTopics(
                    entities = networkTopics.map(NetworkTopic::asEntity)
                )
            }
        )
}
