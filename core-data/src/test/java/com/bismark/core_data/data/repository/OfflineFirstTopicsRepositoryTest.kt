package com.bismark.core_data.data.repository

import com.bismark.core.database.dao.TopicDao
import com.bismark.core.database.model.TopicEntity
import com.bismark.core.database.model.asExternalModel
import com.bismark.core.datastore.test.testUserPreferencesDataStore
import com.bismark.core.model.Topic
import com.bismark.core_data.Synchronizer
import com.bismark.core_data.data.testdoubles.CollectionType
import com.bismark.core_data.data.testdoubles.TestJPGNetwork
import com.bismark.core_data.data.testdoubles.TestTopicDao
import com.bismark.core_data.model.asEntity
import com.bismark.core_datastore.JPGPreferences
import com.bismark.core_network.model.NetworkTopic
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class OfflineFirstTopicsRepositoryTest {

    private lateinit var subject: OfflineFirstTopicsRepository

    private lateinit var topicDao: TopicDao

    private lateinit var network: TestJPGNetwork

    private lateinit var niaPreferences: JPGPreferences

    private lateinit var synchronizer: Synchronizer

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        topicDao = TestTopicDao()
        network = TestJPGNetwork()
        niaPreferences = JPGPreferences(
            tmpFolder.testUserPreferencesDataStore()
        )
        synchronizer = TestSynchronizer(niaPreferences)

        subject = OfflineFirstTopicsRepository(
            topicDao = topicDao,
            network = network,
            niaPreferences = niaPreferences
        )
    }

    @Test
    fun offlineFirstTopicsRepository_topics_stream_is_backed_by_topics_dao() =
        runTest {
            Assert.assertEquals(
                topicDao.getTopicEntitiesStream()
                    .first()
                    .map(TopicEntity::asExternalModel),
                subject.getTopicsStream()
                    .first()
            )
        }

    @Test
    fun offlineFirstTopicsRepository_news_resources_for_interests_is_backed_by_news_resource_dao() =
        runTest {
            Assert.assertEquals(
                niaPreferences.followedTopicIds
                    .first(),
                subject.getFollowedTopicIdsStream()
                    .first()
            )
        }

    @Test
    fun offlineFirstTopicsRepository_sync_pulls_from_network() =
        runTest {
            subject.syncWith(synchronizer)

            val networkTopics = network.getTopics()
                .map(NetworkTopic::asEntity)

            val dbTopics = topicDao.getTopicEntitiesStream()
                .first()

            Assert.assertEquals(
                networkTopics.map(TopicEntity::id),
                dbTopics.map(TopicEntity::id)
            )

            // After sync version should be updated
            Assert.assertEquals(
                network.latestChangeListVersion(CollectionType.Topics),
                synchronizer.getChangeListVersions().topicVersion
            )
        }

    @Test
    fun offlineFirstTopicsRepository_incremental_sync_pulls_from_network() =
        runTest {
            // Set topics version to 10
            synchronizer.updateChangeListVersions {
                copy(topicVersion = 10)
            }

            subject.syncWith(synchronizer)

            val networkTopics = network.getTopics()
                .map(NetworkTopic::asEntity)
                // Drop 10 to simulate the first 10 items being unchanged
                .drop(10)

            val dbTopics = topicDao.getTopicEntitiesStream()
                .first()

            Assert.assertEquals(
                networkTopics.map(TopicEntity::id),
                dbTopics.map(TopicEntity::id)
            )

            // After sync version should be updated
            Assert.assertEquals(
                network.latestChangeListVersion(CollectionType.Topics),
                synchronizer.getChangeListVersions().topicVersion
            )
        }

    @Test
    fun offlineFirstTopicsRepository_sync_deletes_items_marked_deleted_on_network() =
        runTest {
            val networkTopics = network.getTopics()
                .map(NetworkTopic::asEntity)
                .map(TopicEntity::asExternalModel)

            // Delete half of the items on the network
            val deletedItems = networkTopics
                .map(Topic::id)
                .partition { it.chars().sum() % 2 == 0 }
                .first
                .toSet()

            deletedItems.forEach {
                network.editCollection(
                    collectionType = CollectionType.Topics,
                    id = it,
                    isDelete = true
                )
            }

            subject.syncWith(synchronizer)

            val dbTopics = topicDao.getTopicEntitiesStream()
                .first()
                .map(TopicEntity::asExternalModel)

            // Assert that items marked deleted on the network have been deleted locally
            Assert.assertEquals(
                networkTopics.map(Topic::id) - deletedItems,
                dbTopics.map(Topic::id)
            )

            // After sync version should be updated
            Assert.assertEquals(
                network.latestChangeListVersion(CollectionType.Topics),
                synchronizer.getChangeListVersions().topicVersion
            )
        }

    @Test
    fun offlineFirstTopicsRepository_toggle_followed_topics_logic_delegates_to_nia_preferences() =
        runTest {
            subject.toggleFollowedTopicId(followedTopicId = "0", followed = true)

            Assert.assertEquals(
                setOf("0"),
                subject.getFollowedTopicIdsStream()
                    .first()
            )

            subject.toggleFollowedTopicId(followedTopicId = "1", followed = true)

            Assert.assertEquals(
                setOf("0", "1"),
                subject.getFollowedTopicIdsStream()
                    .first()
            )

            Assert.assertEquals(
                niaPreferences.followedTopicIds
                    .first(),
                subject.getFollowedTopicIdsStream()
                    .first()
            )
        }

    @Test
    fun offlineFirstTopicsRepository_set_followed_topics_logic_delegates_to_nia_preferences() =
        runTest {
            subject.setFollowedTopicIds(followedTopicIds = setOf("1", "2"))

            Assert.assertEquals(
                setOf("1", "2"),
                subject.getFollowedTopicIdsStream()
                    .first()
            )

            Assert.assertEquals(
                niaPreferences.followedTopicIds
                    .first(),
                subject.getFollowedTopicIdsStream()
                    .first()
            )
        }
}
