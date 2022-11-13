package com.bismark.core_data.data.repository

import com.bismark.core.database.model.AuthorEntity
import com.bismark.core.database.model.EpisodeEntity
import com.bismark.core.database.model.NewsResourceEntity
import com.bismark.core.database.model.PopulatedEpisode
import com.bismark.core.database.model.PopulatedNewsResource
import com.bismark.core.database.model.TopicEntity
import com.bismark.core.database.model.asExternalModel
import com.bismark.core.datastore.test.testUserPreferencesDataStore
import com.bismark.core.model.NewsResource
import com.bismark.core_data.Synchronizer
import com.bismark.core_data.data.testdoubles.CollectionType
import com.bismark.core_data.data.testdoubles.TestAuthorDao
import com.bismark.core_data.data.testdoubles.TestEpisodeDao
import com.bismark.core_data.data.testdoubles.TestJPGNetwork
import com.bismark.core_data.data.testdoubles.TestNewsResourceDao
import com.bismark.core_data.data.testdoubles.TestTopicDao
import com.bismark.core_data.data.testdoubles.filteredInterestsIds
import com.bismark.core_data.data.testdoubles.nonPresentInterestsIds
import com.bismark.core_data.model.asEntity
import com.bismark.core_data.model.authorCrossReferences
import com.bismark.core_data.model.authorEntityShells
import com.bismark.core_data.model.episodeEntityShell
import com.bismark.core_data.model.topicCrossReferences
import com.bismark.core_data.model.topicEntityShells
import com.bismark.core_datastore.JPGPreferences
import com.bismark.core_network.model.NetworkChangeList
import com.bismark.core_network.model.NetworkNewsResource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class OfflineFirstNewsRepositoryTest {

    private lateinit var subject: OfflineFirstNewsRepository

    private lateinit var newsResourceDao: TestNewsResourceDao

    private lateinit var episodeDao: TestEpisodeDao

    private lateinit var authorDao: TestAuthorDao

    private lateinit var topicDao: TestTopicDao

    private lateinit var network: TestJPGNetwork

    private lateinit var synchronizer: Synchronizer

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        newsResourceDao = TestNewsResourceDao()
        episodeDao = TestEpisodeDao()
        authorDao = TestAuthorDao()
        topicDao = TestTopicDao()
        network = TestJPGNetwork()
        synchronizer = TestSynchronizer(
            JPGPreferences(
                tmpFolder.testUserPreferencesDataStore()
            )
        )

        subject = OfflineFirstNewsRepository(
            newsResourceDao = newsResourceDao,
            episodeDao = episodeDao,
            authorDao = authorDao,
            topicDao = topicDao,
            network = network,
        )
    }

    @Test
    fun offlineFirstNewsRepository_news_resources_stream_is_backed_by_news_resource_dao() =
        runTest {
            assertEquals(
                newsResourceDao.getNewsResourcesStream()
                    .first()
                    .map(PopulatedNewsResource::asExternalModel),
                subject.getNewsResourcesStream()
                    .first()
            )
        }

    @Test
    fun offlineFirstNewsRepository_news_resources_for_topic_is_backed_by_news_resource_dao() =
        runTest {
            assertEquals(
                newsResourceDao.getNewsResourcesStream(
                    filterTopicIds = filteredInterestsIds,
                )
                    .first()
                    .map(PopulatedNewsResource::asExternalModel),
                subject.getNewsResourcesStream(
                    filterTopicIds = filteredInterestsIds,
                )
                    .first()
            )

            assertEquals(
                emptyList<NewsResource>(),
                subject.getNewsResourcesStream(
                    filterTopicIds = nonPresentInterestsIds,
                )
                    .first()
            )
        }

    @Test
    fun offlineFirstNewsRepository_news_resources_for_author_is_backed_by_news_resource_dao() =
        runTest {
            assertEquals(
                newsResourceDao.getNewsResourcesStream(
                    filterAuthorIds = filteredInterestsIds
                )
                    .first()
                    .map(PopulatedNewsResource::asExternalModel),
                subject.getNewsResourcesStream(
                    filterAuthorIds = filteredInterestsIds
                )
                    .first()
            )

            assertEquals(
                emptyList<NewsResource>(),
                subject.getNewsResourcesStream(
                    filterAuthorIds = nonPresentInterestsIds
                )
                    .first()
            )
        }

    @Test
    fun offlineFirstNewsRepository_sync_pulls_from_network() =
        runTest {
            subject.syncWith(synchronizer)

            val newsResourcesFromNetwork = network.getNewsResources()
                .map(NetworkNewsResource::asEntity)
                .map(NewsResourceEntity::asExternalModel)

            val newsResourcesFromDb = newsResourceDao.getNewsResourcesStream()
                .first()
                .map(PopulatedNewsResource::asExternalModel)

            assertEquals(
                newsResourcesFromNetwork.map(NewsResource::id),
                newsResourcesFromDb.map(NewsResource::id)
            )

            // After sync version should be updated
            assertEquals(
                network.latestChangeListVersion(CollectionType.NewsResources),
                synchronizer.getChangeListVersions().newsResourceVersion
            )
        }

    @Test
    fun offlineFirstNewsRepository_sync_deletes_items_marked_deleted_on_network() =
        runTest {
            val newsResourcesFromNetwork = network.getNewsResources()
                .map(NetworkNewsResource::asEntity)
                .map(NewsResourceEntity::asExternalModel)

            // Delete half of the items on the network
            val deletedItems = newsResourcesFromNetwork
                .map(NewsResource::id)
                .partition { it.chars().sum() % 2 == 0 }
                .first
                .toSet()

            deletedItems.forEach {
                network.editCollection(
                    collectionType = CollectionType.NewsResources,
                    id = it,
                    isDelete = true
                )
            }

            subject.syncWith(synchronizer)

            val newsResourcesFromDb = newsResourceDao.getNewsResourcesStream()
                .first()
                .map(PopulatedNewsResource::asExternalModel)

            // Assert that items marked deleted on the network have been deleted locally
            assertEquals(
                newsResourcesFromNetwork.map(NewsResource::id) - deletedItems,
                newsResourcesFromDb.map(NewsResource::id)
            )

            // After sync version should be updated
            assertEquals(
                network.latestChangeListVersion(CollectionType.NewsResources),
                synchronizer.getChangeListVersions().newsResourceVersion
            )
        }

    @Test
    fun offlineFirstNewsRepository_incremental_sync_pulls_from_network() =
        runTest {
            // Set news version to 7
            synchronizer.updateChangeListVersions {
                copy(newsResourceVersion = 7)
            }

            subject.syncWith(synchronizer)

            val changeList = network.changeListsAfter(
                CollectionType.NewsResources,
                version = 7
            )
            val changeListIds = changeList
                .map(NetworkChangeList::id)
                .toSet()

            val newsResourcesFromNetwork = network.getNewsResources()
                .map(NetworkNewsResource::asEntity)
                .map(NewsResourceEntity::asExternalModel)
                .filter { it.id in changeListIds }

            val newsResourcesFromDb = newsResourceDao.getNewsResourcesStream()
                .first()
                .map(PopulatedNewsResource::asExternalModel)

            assertEquals(
                newsResourcesFromNetwork.map(NewsResource::id),
                newsResourcesFromDb.map(NewsResource::id)
            )

            // After sync version should be updated
            assertEquals(
                changeList.last().changeListVersion,
                synchronizer.getChangeListVersions().newsResourceVersion
            )
        }

    @Test
    fun offlineFirstNewsRepository_sync_saves_shell_topic_entities() =
        runTest {
            subject.syncWith(synchronizer)

            assertEquals(
                network.getNewsResources()
                    .map(NetworkNewsResource::topicEntityShells)
                    .flatten()
                    .distinctBy(TopicEntity::id),
                topicDao.getTopicEntitiesStream()
                    .first()
            )
        }

    @Test
    fun offlineFirstNewsRepository_sync_saves_shell_author_entities() =
        runTest {
            subject.syncWith(synchronizer)

            assertEquals(
                network.getNewsResources()
                    .map(NetworkNewsResource::authorEntityShells)
                    .flatten()
                    .distinctBy(AuthorEntity::id),
                authorDao.getAuthorEntitiesStream()
                    .first()
            )
        }

    @Test
    fun offlineFirstNewsRepository_sync_saves_shell_episode_entities() =
        runTest {
            subject.syncWith(synchronizer)

            assertEquals(
                network.getNewsResources()
                    .map(NetworkNewsResource::episodeEntityShell)
                    .distinctBy(EpisodeEntity::id),
                episodeDao.getEpisodesStream()
                    .first()
                    .map(PopulatedEpisode::entity)
            )
        }

    @Test
    fun offlineFirstNewsRepository_sync_saves_topic_cross_references() =
        runTest {
            subject.syncWith(synchronizer)

            assertEquals(
                network.getNewsResources()
                    .map(NetworkNewsResource::topicCrossReferences)
                    .distinct()
                    .flatten(),
                newsResourceDao.topicCrossReferences
            )
        }

    @Test
    fun offlineFirstNewsRepository_sync_saves_author_cross_references() =
        runTest {
            subject.syncWith(synchronizer)

            assertEquals(
                network.getNewsResources()
                    .map(NetworkNewsResource::authorCrossReferences)
                    .distinct()
                    .flatten(),
                newsResourceDao.authorCrossReferences
            )
        }
}
