package com.bismark.feature.interests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bismark.core.model.FollowableAuthor
import com.bismark.core.model.FollowableTopic
import com.bismark.core_data.repository.AuthorsRepository
import com.bismark.core_data.repository.TopicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestsViewModel @Inject constructor(
    private val authorsRepository: AuthorsRepository,
    private val topicsRepository: TopicsRepository
) : ViewModel() {

    private val _tabState = MutableStateFlow(
        InterestsTabState(
            titles = listOf(R.string.interests_topics, R.string.interests_people),
            currentIndex = 0
        )
    )
    val tabState: StateFlow<InterestsTabState> = _tabState.asStateFlow()

    val uiState: StateFlow<InterestsUiState> = combine(
        authorsRepository.getAuthorsStream(),
        authorsRepository.getFollowedAuthorIdsStream(),
        topicsRepository.getTopicsStream(),
        topicsRepository.getFollowedTopicIdsStream(),
    ) { availableAuthors, followedAuthorIdsState, availableTopics, followedTopicIdsState ->

        InterestsUiState.Interests(
            authors = availableAuthors
                .map { author ->
                    FollowableAuthor(
                        author = author,
                        isFollowed = author.id in followedAuthorIdsState
                    )
                }
                .sortedBy { it.author.name },
            topics = availableTopics
                .map { topic ->
                    FollowableTopic(
                        topic = topic,
                        isFollowed = topic.id in followedTopicIdsState
                    )
                }
                .sortedBy { it.topic.name }
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InterestsUiState.Loading
        )

    fun followTopic(followedTopicId: String, followed: Boolean) {
        viewModelScope.launch {
            topicsRepository.toggleFollowedTopicId(followedTopicId, followed)
        }
    }

    fun followAuthor(followedAuthorId: String, followed: Boolean) {
        viewModelScope.launch {
            authorsRepository.toggleFollowedAuthorId(followedAuthorId, followed)
        }
    }

    fun switchTab(newIndex: Int) {
        if (newIndex != tabState.value.currentIndex) {
            _tabState.update {
                it.copy(currentIndex = newIndex)
            }
        }
    }
}

data class InterestsTabState(
    val titles: List<Int>,
    val currentIndex: Int
)

sealed interface InterestsUiState {
    object Loading : InterestsUiState

    data class Interests(
        val authors: List<FollowableAuthor>,
        val topics: List<FollowableTopic>
    ) : InterestsUiState

    object Empty : InterestsUiState
}
