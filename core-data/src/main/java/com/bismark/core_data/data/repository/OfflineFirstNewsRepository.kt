package com.bismark.core_data.data.repository

import com.bismark.core.database.dao.AuthorDao
import com.bismark.core.database.dao.EpisodeDao
import com.bismark.core.database.dao.NewsResourceDao
import com.bismark.core.database.dao.TopicDao
import com.bismark.core.database.model.AuthorEntity
import com.bismark.core.database.model.EpisodeEntity
import com.bismark.core.database.model.PopulatedNewsResource
import com.bismark.core.database.model.TopicEntity
import com.bismark.core.database.model.asExternalModel
import com.bismark.core.model.NewsResource
import com.bismark.core_data.Synchronizer
import com.bismark.core_data.changeListSync
import com.bismark.core_data.model.asEntity
import com.bismark.core_data.model.authorCrossReferences
import com.bismark.core_data.model.authorEntityShells
import com.bismark.core_data.model.episodeEntityShell
import com.bismark.core_data.model.topicCrossReferences
import com.bismark.core_data.model.topicEntityShells
import com.bismark.core_datastore.ChangeListVersions
import com.bismark.core_network.JPGNetwork
import com.bismark.core_network.model.NetworkNewsResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Disk storage backed implementation of the [NewsRepository].
 * Reads are exclusively from local storage to support offline access.
 */
class OfflineFirstNewsRepository @Inject constructor(
    private val newsResourceDao: NewsResourceDao,
    private val episodeDao: EpisodeDao,
    private val authorDao: AuthorDao,
    private val topicDao: TopicDao,
    private val network: JPGNetwork,
) : NewsRepository {

    override fun getNewsResourcesStream(): Flow<List<NewsResource>> =
        newsResourceDao.getNewsResourcesStream()
            .map { it.map(PopulatedNewsResource::asExternalModel) }

    override fun getNewsResourcesStream(
        filterAuthorIds: Set<String>,
        filterTopicIds: Set<String>
    ): Flow<List<NewsResource>> = newsResourceDao.getNewsResourcesStream(
        filterAuthorIds = filterAuthorIds,
        filterTopicIds = filterTopicIds
    )
        .map { it.map(PopulatedNewsResource::asExternalModel) }

    override suspend fun syncWith(synchronizer: Synchronizer) =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::newsResourceVersion,
            changeListFetcher = { currentVersion ->
                network.getNewsResourceChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(newsResourceVersion = latestVersion)
            },
            modelDeleter = newsResourceDao::deleteNewsResources,
            modelUpdater = { changedIds ->
                val networkNewsResources = network.getNewsResources(ids = changedIds)

                // Order of invocation matters to satisfy id and foreign key constraints!

                topicDao.insertOrIgnoreTopics(
                    topicEntities = networkNewsResources
                        .map(NetworkNewsResource::topicEntityShells)
                        .flatten()
                        .distinctBy(TopicEntity::id)
                )
                authorDao.insertOrIgnoreAuthors(
                    authorEntities = networkNewsResources
                        .map(NetworkNewsResource::authorEntityShells)
                        .flatten()
                        .distinctBy(AuthorEntity::id)
                )
                episodeDao.insertOrIgnoreEpisodes(
                    episodeEntities = networkNewsResources
                        .map(NetworkNewsResource::episodeEntityShell)
                        .distinctBy(EpisodeEntity::id)
                )
                newsResourceDao.upsertNewsResources(
                    newsResourceEntities = networkNewsResources
                        .map(NetworkNewsResource::asEntity)
                )
                newsResourceDao.insertOrIgnoreTopicCrossRefEntities(
                    newsResourceTopicCrossReferences = networkNewsResources
                        .map(NetworkNewsResource::topicCrossReferences)
                        .distinct()
                        .flatten()
                )
                newsResourceDao.insertOrIgnoreAuthorCrossRefEntities(
                    newsResourceAuthorCrossReferences = networkNewsResources
                        .map(NetworkNewsResource::authorCrossReferences)
                        .distinct()
                        .flatten()
                )
            }
        )
}
