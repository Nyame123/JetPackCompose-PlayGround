package com.bismark.core_data.database.model

import com.bismark.core.database.model.AuthorEntity
import com.bismark.core.database.model.EpisodeEntity
import com.bismark.core.database.model.NewsResourceEntity
import com.bismark.core.database.model.PopulatedEpisode
import com.bismark.core.database.model.asExternalModel
import com.bismark.core.model.Author
import com.bismark.core.model.Episode
import com.bismark.core.model.NewsResource
import com.bismark.core.model.NewsResourceType
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import org.junit.Test

class PopulatedEpisodeKtTest {

    @Test
    fun populated_episode_can_be_mapped_to_episode() {
        val populatedEpisode = PopulatedEpisode(
            entity = EpisodeEntity(
                id = "0",
                name = "Test",
                publishDate = Instant.fromEpochMilliseconds(1),
                alternateAudio = "audio",
                alternateVideo = "video"
            ),
            newsResources = listOf(
                NewsResourceEntity(
                    id = "1",
                    episodeId = "0",
                    title = "news",
                    content = "Hilt",
                    url = "url",
                    headerImageUrl = "headerImageUrl",
                    type = NewsResourceType.Video,
                    publishDate = Instant.fromEpochMilliseconds(1),
                )
            ),
            authors = listOf(
                AuthorEntity(
                    id = "2",
                    name = "name",
                    imageUrl = "imageUrl",
                    twitter = "twitter",
                    mediumPage = "mediumPage",
                    bio = "bio",
                )
            ),
        )
        val episode = populatedEpisode.asExternalModel()

        assertEquals(
            Episode(
                id = "0",
                name = "Test",
                publishDate = Instant.fromEpochMilliseconds(1),
                alternateAudio = "audio",
                alternateVideo = "video",
                newsResources = listOf(
                    NewsResource(
                        id = "1",
                        episodeId = "0",
                        title = "news",
                        content = "Hilt",
                        url = "url",
                        headerImageUrl = "headerImageUrl",
                        type = NewsResourceType.Video,
                        publishDate = Instant.fromEpochMilliseconds(1),
                        authors = listOf(),
                        topics = listOf()
                    )
                ),
                authors = listOf(
                    Author(
                        id = "2",
                        name = "name",
                        imageUrl = "imageUrl",
                        twitter = "twitter",
                        mediumPage = "mediumPage",
                        bio = "bio",
                    )
                ),
            ),
            episode
        )
    }
}
