package com.bismark.core_data.data.repository

import com.bismark.core_data.Synchronizer
import com.bismark.core_datastore.ChangeListVersions
import com.bismark.core_datastore.JPGPreferences

/**
 * Test synchronizer that delegates to [JPGPreferences]
 */
class TestSynchronizer(
    private val niaPreferences: JPGPreferences
) : Synchronizer {

    override suspend fun getChangeListVersions(): ChangeListVersions =
        niaPreferences.getChangeListVersions()

    override suspend fun updateChangeListVersions(
        update: ChangeListVersions.() -> ChangeListVersions
    ) = niaPreferences.updateChangeListVersion(update)
}
