package com.bismark.core_data.repository

import com.bismark.core.database.dao.AuthorDao
import com.bismark.core.database.model.AuthorEntity
import com.bismark.core.database.model.asExternalModel
import com.bismark.core.model.Author
import com.bismark.core_data.Synchronizer
import com.bismark.core_data.changeListSync
import com.bismark.core_data.model.asEntity
import com.bismark.core_datastore.ChangeListVersions
import com.bismark.core_datastore.JPGPreferences
import com.bismark.core_network.JPGNetwork
import com.bismark.core_network.model.NetworkAuthor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Disk storage backed implementation of the [AuthorsRepository].
 * Reads are exclusively from local storage to support offline access.
 */
class OfflineFirstAuthorsRepository @Inject constructor(
    private val authorDao: AuthorDao,
    private val network: JPGNetwork,
    private val niaPreferences: JPGPreferences,
) : AuthorsRepository {

    override fun getAuthorStream(id: String): Flow<Author> =
        authorDao.getAuthorEntityStream(id).map {
            it.asExternalModel()
        }

    override fun getAuthorsStream(): Flow<List<Author>> =
        authorDao.getAuthorEntitiesStream()
            .map { it.map(AuthorEntity::asExternalModel) }

    override suspend fun setFollowedAuthorIds(followedAuthorIds: Set<String>) =
        niaPreferences.setFollowedAuthorIds(followedAuthorIds)

    override suspend fun toggleFollowedAuthorId(followedAuthorId: String, followed: Boolean) =
        niaPreferences.toggleFollowedAuthorId(followedAuthorId, followed)

    override fun getFollowedAuthorIdsStream(): Flow<Set<String>> = niaPreferences.followedAuthorIds

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::authorVersion,
            changeListFetcher = { currentVersion ->
                network.getAuthorChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(authorVersion = latestVersion)
            },
            modelDeleter = authorDao::deleteAuthors,
            modelUpdater = { changedIds ->
                val networkAuthors = network.getAuthors(ids = changedIds)
                authorDao.upsertAuthors(
                    entities = networkAuthors.map(NetworkAuthor::asEntity)
                )
            }
        )
}
