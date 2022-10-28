package com.bismark.core_data.model

import com.bismark.core.database.model.AuthorEntity
import com.bismark.core_network.model.NetworkAuthor

fun NetworkAuthor.asEntity() = AuthorEntity(
    id = id,
    name = name,
    imageUrl = imageUrl,
    twitter = twitter,
    mediumPage = mediumPage,
    bio = bio,
)
