package com.bismark.feature.foryou

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.bismark.core.model.FollowableAuthor
import com.bismark.core.model.FollowableTopic
import com.bismark.core.model.NewsResource
import com.bismark.core.model.SaveableNewsResource
import com.bismark.core_data.repository.AuthorsRepository
import com.bismark.core_data.repository.NewsRepository
import com.bismark.core_data.repository.TopicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val authorsRepository: AuthorsRepository,
    private val topicsRepository: TopicsRepository,
    private val newsRepository: NewsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val followedInterestsState: StateFlow<FollowedInterestsState> =
        combine(
            authorsRepository.getFollowedAuthorIdsStream(),
            topicsRepository.getFollowedTopicIdsStream(),
        ) { followedAuthors, followedTopics ->
            if (followedAuthors.isEmpty() && followedTopics.isEmpty()) {
                FollowedInterestsState.None
            } else {
                FollowedInterestsState.FollowedInterests(
                    authorIds = followedAuthors,
                    topicIds = followedTopics
                )
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FollowedInterestsState.Unknown
            )

    /**
     * TODO: Temporary saving of news resources persisted through process death with a
     *       [SavedStateHandle].
     *
     * This should be persisted to disk instead.
     */
    private var savedNewsResources by savedStateHandle.saveable {
        mutableStateOf<Set<String>>(emptySet())
    }

    /**
     * The in-progress set of topics to be selected, persisted through process death with a
     * [SavedStateHandle].
     */
    private var inProgressTopicSelection by savedStateHandle.saveable {
        mutableStateOf<Set<String>>(emptySet())
    }

    /**
     * The in-progress set of authors to be selected, persisted through process death with a
     * [SavedStateHandle].
     */
    private var inProgressAuthorSelection by savedStateHandle.saveable {
        mutableStateOf<Set<String>>(emptySet())
    }

    val feedState: StateFlow<ForYouFeedState> =
        combine(
            followedInterestsState,
            snapshotFlow { inProgressTopicSelection },
            snapshotFlow { inProgressAuthorSelection },
            snapshotFlow { savedNewsResources }
        ) { followedInterestsUserState, inProgressTopicSelection, inProgressAuthorSelection,
            savedNewsResources ->

            when (followedInterestsUserState) {
                // If we don't know the current selection state, emit loading.
                FollowedInterestsState.Unknown -> flowOf<ForYouFeedState>(ForYouFeedState.Loading)
                // If the user has followed topics, use those followed topics to populate the feed
                is FollowedInterestsState.FollowedInterests -> {
                    newsRepository.getNewsResourcesStream(
                        filterTopicIds = followedInterestsUserState.topicIds,
                        filterAuthorIds = followedInterestsUserState.authorIds
                    ).mapToFeedState(savedNewsResources)
                }
                // If the user hasn't followed interests yet, show a realtime populated feed based
                // on the in-progress interests selections, if there are any.
                FollowedInterestsState.None -> {
                    if (inProgressTopicSelection.isEmpty() && inProgressAuthorSelection.isEmpty()) {
                        flowOf<ForYouFeedState>(ForYouFeedState.Success(emptyList()))
                    } else {
                        newsRepository.getNewsResourcesStream(
                            filterTopicIds = inProgressTopicSelection,
                            filterAuthorIds = inProgressAuthorSelection
                        ).mapToFeedState(savedNewsResources)
                    }
                }
            }
        }
            // Flatten the feed flows.
            // As the selected topics and topic state changes, this will cancel the old feed
            // monitoring and start the new one.
            .flatMapLatest { it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ForYouFeedState.Loading
            )

    val interestsSelectionState: StateFlow<ForYouInterestsSelectionState> =
        combine(
            followedInterestsState,
            topicsRepository.getTopicsStream(),
            authorsRepository.getAuthorsStream(),
            snapshotFlow { inProgressTopicSelection },
            snapshotFlow { inProgressAuthorSelection },
        ) { followedInterestsUserState, availableTopics, availableAuthors, inProgressTopicSelection,
            inProgressAuthorSelection ->

            when (followedInterestsUserState) {
                FollowedInterestsState.Unknown -> ForYouInterestsSelectionState.Loading
                is FollowedInterestsState.FollowedInterests -> ForYouInterestsSelectionState.NoInterestsSelection
                FollowedInterestsState.None -> {
                    val topics = availableTopics.map { topic ->
                        FollowableTopic(
                            topic = topic,
                            isFollowed = topic.id in inProgressTopicSelection
                        )
                    }
                    val authors = availableAuthors.map { author ->
                        FollowableAuthor(
                            author = author,
                            isFollowed = author.id in inProgressAuthorSelection
                        )
                    }

                    if (topics.isEmpty() && authors.isEmpty()) {
                        ForYouInterestsSelectionState.Loading
                    } else {
                        ForYouInterestsSelectionState.WithInterestsSelection(
                            topics = topics,
                            authors = authors
                        )
                    }
                }
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ForYouInterestsSelectionState.Loading
            )

    fun updateTopicSelection(topicId: String, isChecked: Boolean) {
        Snapshot.withMutableSnapshot {
            inProgressTopicSelection =
                    // Update the in-progress selection based on whether the topic id was checked
                if (isChecked) {
                    inProgressTopicSelection + topicId
                } else {
                    inProgressTopicSelection - topicId
                }
        }
    }

    fun updateAuthorSelection(authorId: String, isChecked: Boolean) {
        Snapshot.withMutableSnapshot {
            inProgressAuthorSelection =
                    // Update the in-progress selection based on whether the author id was checked
                if (isChecked) {
                    inProgressAuthorSelection + authorId
                } else {
                    inProgressAuthorSelection - authorId
                }
        }
    }

    fun updateNewsResourceSaved(newsResourceId: String, isChecked: Boolean) {
        Snapshot.withMutableSnapshot {
            savedNewsResources =
                if (isChecked) {
                    savedNewsResources + newsResourceId
                } else {
                    savedNewsResources - newsResourceId
                }
        }
    }

    fun saveFollowedInterests() {
        // Don't attempt to save anything if nothing is selected
        if (inProgressTopicSelection.isEmpty() && inProgressAuthorSelection.isEmpty()) {
            return
        }

        viewModelScope.launch {
            topicsRepository.setFollowedTopicIds(inProgressTopicSelection)
            authorsRepository.setFollowedAuthorIds(inProgressAuthorSelection)
            // Clear out the old selection, in case we return to onboarding
            Snapshot.withMutableSnapshot {
                inProgressTopicSelection = emptySet()
                inProgressAuthorSelection = emptySet()
            }
        }
    }
}

private fun Flow<List<NewsResource>>.mapToFeedState(
    savedNewsResources: Set<String>
): Flow<ForYouFeedState> =
    filterNot { it.isEmpty() }
        .map { newsResources ->
            newsResources.map { newsResource ->
                SaveableNewsResource(
                    newsResource = newsResource,
                    isSaved = savedNewsResources.contains(newsResource.id)
                )
            }
        }
        .map<List<SaveableNewsResource>, ForYouFeedState>(ForYouFeedState::Success)
        .onStart { emit(ForYouFeedState.Loading) }

