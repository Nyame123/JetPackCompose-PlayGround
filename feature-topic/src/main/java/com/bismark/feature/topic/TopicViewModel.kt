package com.bismark.feature.topic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bismark.core.common.result.Result
import com.bismark.core.common.result.asResult
import com.bismark.core.model.FollowableTopic
import com.bismark.core.model.NewsResource
import com.bismark.core.model.Topic
import com.bismark.core_data.repository.NewsRepository
import com.bismark.core_data.repository.TopicsRepository
import com.bismark.feature.topic.navigation.TopicDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val topicsRepository: TopicsRepository,
    newsRepository: NewsRepository
) : ViewModel() {

    private val topicId: String = checkNotNull(savedStateHandle[TopicDestination.topicIdArg])

    // Observe the followed topics, as they could change over time.
    private val followedTopicIdsStream: Flow<Result<Set<String>>> =
        topicsRepository.getFollowedTopicIdsStream().asResult()

    // Observe topic information
    private val topic: Flow<Result<Topic>> = topicsRepository.getTopic(topicId).asResult()

    // Observe the News for this topic
    private val newsStream: Flow<Result<List<NewsResource>>> =
        newsRepository.getNewsResourcesStream(
            filterTopicIds = setOf(element = topicId),
        ).asResult()

    val uiState: StateFlow<TopicScreenUiState> =
        combine(
            followedTopicIdsStream,
            topic,
            newsStream
        ) { followedTopicsResult, topicResult, newsResult ->
            val topic: TopicUiState =
                if (topicResult is Result.Success && followedTopicsResult is Result.Success) {
                    val followed = followedTopicsResult.data.contains(topicId)
                    TopicUiState.Success(
                        followableTopic = FollowableTopic(
                            topic = topicResult.data,
                            isFollowed = followed
                        )
                    )
                } else if (
                    topicResult is Result.Loading || followedTopicsResult is Result.Loading
                ) {
                    TopicUiState.Loading
                } else {
                    TopicUiState.Error
                }

            val news: NewsUiState = when (newsResult) {
                is Result.Success -> NewsUiState.Success(newsResult.data)
                is Result.Loading -> NewsUiState.Loading
                is Result.Error -> NewsUiState.Error
            }

            TopicScreenUiState(topic, news)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = TopicScreenUiState(TopicUiState.Loading, NewsUiState.Loading)
            )

    fun followTopicToggle(followed: Boolean) {
        viewModelScope.launch {
            topicsRepository.toggleFollowedTopicId(topicId, followed)
        }
    }
}

sealed interface TopicUiState {
    data class Success(val followableTopic: FollowableTopic) : TopicUiState
    object Error : TopicUiState
    object Loading : TopicUiState
}

sealed interface NewsUiState {
    data class Success(val news: List<NewsResource>) : NewsUiState
    object Error : NewsUiState
    object Loading : NewsUiState
}

data class TopicScreenUiState(
    val topicState: TopicUiState,
    val newsState: NewsUiState
)
