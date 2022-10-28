package com.bismark.core_data.model

import com.bismark.core.database.model.EpisodeEntity
import com.bismark.core_network.model.NetworkEpisode
import com.bismark.core_network.model.NetworkEpisodeExpanded

fun NetworkEpisode.asEntity() = EpisodeEntity(
    id = id,
    name = name,
    publishDate = publishDate,
    alternateVideo = alternateVideo,
    alternateAudio = alternateAudio,
)

fun NetworkEpisodeExpanded.asEntity() = EpisodeEntity(
    id = id,
    name = name,
    publishDate = publishDate,
    alternateVideo = alternateVideo,
    alternateAudio = alternateAudio,
)
