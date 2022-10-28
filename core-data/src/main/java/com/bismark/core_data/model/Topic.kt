package com.bismark.core_data.model

import com.bismark.core.database.model.TopicEntity
import com.bismark.core_network.model.NetworkTopic

fun NetworkTopic.asEntity() = TopicEntity(
    id = id,
    name = name,
    shortDescription = shortDescription,
    longDescription = longDescription,
    url = url,
    imageUrl = imageUrl
)
