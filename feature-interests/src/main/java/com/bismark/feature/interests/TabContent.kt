package com.bismark.feature.interests

import InterestsItem
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bismark.core.model.FollowableAuthor
import com.bismark.core.model.FollowableTopic

@Composable
fun TopicsTabContent(
    topics: List<FollowableTopic>,
    onTopicClick: (String) -> Unit,
    onFollowButtonClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        topics.forEach { followableTopic ->
            item {
                InterestsItem(
                    name = followableTopic.topic.name,
                    following = followableTopic.isFollowed,
                    description = followableTopic.topic.shortDescription,
                    topicImageUrl = followableTopic.topic.imageUrl,
                    onClick = { onTopicClick(followableTopic.topic.id) },
                    onFollowButtonClick = { onFollowButtonClick(followableTopic.topic.id, it) }
                )
            }
        }

        item {
            Spacer(
                // TODO: Replace with windowInsetsBottomHeight after
                //       https://issuetracker.google.com/issues/230383055
                Modifier.windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                )
            )
        }
    }
}

@Composable
fun AuthorsTabContent(
    authors: List<FollowableAuthor>,
    onAuthorClick: (String) -> Unit,
    onFollowButtonClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        authors.forEach { followableAuthor ->
            item {
                InterestsItem(
                    name = followableAuthor.author.name,
                    following = followableAuthor.isFollowed,
                    topicImageUrl = followableAuthor.author.imageUrl,
                    onClick = { onAuthorClick(followableAuthor.author.id) },
                    onFollowButtonClick = { onFollowButtonClick(followableAuthor.author.id, it) },
                    iconModifier = Modifier.clip(CircleShape)
                )
            }
        }

        item {
            Spacer(
                // TODO: Replace with windowInsetsBottomHeight after
                //       https://issuetracker.google.com/issues/230383055
                Modifier.windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                )
            )
        }
    }
}
