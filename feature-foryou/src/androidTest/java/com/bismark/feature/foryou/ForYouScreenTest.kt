package com.bismark.feature.foryou

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.unit.DpSize
import com.bismark.core.model.Author
import com.bismark.core.model.FollowableAuthor
import com.bismark.core.model.FollowableTopic
import com.bismark.core.model.NewsResource
import com.bismark.core.model.NewsResourceType
import com.bismark.core.model.SaveableNewsResource
import com.bismark.core.model.Topic
import kotlinx.datetime.Instant
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class ForYouScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val doneButtonMatcher by lazy {
        hasText(
            composeTestRule.activity.resources.getString(R.string.done)
        )
    }

    @Test
    fun circularProgressIndicator_whenScreenIsLoading_exists() {
        composeTestRule.setContent {
            BoxWithConstraints {
                ForYouScreen(
                    windowSizeClass = WindowSizeClass.calculateFromSize(
                        DpSize(maxWidth, maxHeight)
                    ),
                    interestsSelectionState = ForYouInterestsSelectionState.Loading,
                    feedState = ForYouFeedState.Loading,
                    onAuthorCheckedChanged = { _, _ -> },
                    onTopicCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.for_you_loading)
            )
            .assertExists()
    }

    @Test
    fun topicSelector_whenNoTopicsSelected_showsTopicChipsAndDisabledDoneButton() {
        composeTestRule.setContent {
            BoxWithConstraints {
                ForYouScreen(
                    windowSizeClass = WindowSizeClass.calculateFromSize(
                        DpSize(maxWidth, maxHeight)
                    ),
                    interestsSelectionState = ForYouInterestsSelectionState.WithInterestsSelection(
                        topics = listOf(
                            FollowableTopic(
                                topic = Topic(
                                    id = "0",
                                    name = "Headlines",
                                    shortDescription = "",
                                    longDescription = "",
                                    url = "",
                                    imageUrl = ""
                                ),
                                isFollowed = false
                            ),
                            FollowableTopic(
                                topic = Topic(
                                    id = "1",
                                    name = "UI",
                                    shortDescription = "",
                                    longDescription = "",
                                    url = "",
                                    imageUrl = ""
                                ),
                                isFollowed = false
                            ),
                            FollowableTopic(
                                topic = Topic(
                                    id = "2",
                                    name = "Tools",
                                    shortDescription = "",
                                    longDescription = "",
                                    url = "",
                                    imageUrl = ""
                                ),
                                isFollowed = false
                            ),
                        ),
                        authors = listOf(
                            FollowableAuthor(
                                author = Author(
                                    id = "0",
                                    name = "Android Dev",
                                    imageUrl = "",
                                    twitter = "",
                                    mediumPage = "",
                                    bio = "",
                                ),
                                isFollowed = false
                            ),
                            FollowableAuthor(
                                author = Author(
                                    id = "1",
                                    name = "Android Dev 2",
                                    imageUrl = "",
                                    twitter = "",
                                    mediumPage = "",
                                    bio = "",
                                ),
                                isFollowed = false
                            ),
                        )
                    ),
                    feedState = ForYouFeedState.Success(
                        feed = emptyList()
                    ),
                    onAuthorCheckedChanged = { _, _ -> },
                    onTopicCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        composeTestRule
            .onNodeWithText("Headlines")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("UI")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("Tools")
            .assertIsDisplayed()
            .assertHasClickAction()

        // Scroll until the Done button is visible
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(doneButtonMatcher)

        composeTestRule
            .onNode(doneButtonMatcher)
            .assertIsDisplayed()
            .assertIsNotEnabled()
            .assertHasClickAction()
    }

    @Test
    fun topicSelector_whenSomeTopicsSelected_showsTopicChipsAndEnabledDoneButton() {
        composeTestRule.setContent {
            BoxWithConstraints {
                ForYouScreen(
                    windowSizeClass = WindowSizeClass.calculateFromSize(
                        DpSize(maxWidth, maxHeight)
                    ),
                    interestsSelectionState = ForYouInterestsSelectionState.WithInterestsSelection(
                        topics = listOf(
                            FollowableTopic(
                                topic = Topic(
                                    id = "0",
                                    name = "Headlines",
                                    shortDescription = "",
                                    longDescription = "",
                                    url = "",
                                    imageUrl = ""
                                ),
                                isFollowed = false
                            ),
                            FollowableTopic(
                                topic = Topic(
                                    id = "1",
                                    name = "UI",
                                    shortDescription = "",
                                    longDescription = "",
                                    url = "",
                                    imageUrl = ""
                                ),
                                isFollowed = true
                            ),
                            FollowableTopic(
                                topic = Topic(
                                    id = "2",
                                    name = "Tools",
                                    shortDescription = "",
                                    longDescription = "",
                                    url = "",
                                    imageUrl = ""
                                ),
                                isFollowed = false
                            ),
                        ),
                        authors = listOf(
                            FollowableAuthor(
                                author = Author(
                                    id = "0",
                                    name = "Android Dev",
                                    imageUrl = "",
                                    twitter = "",
                                    mediumPage = "",
                                    bio = "",
                                ),
                                isFollowed = false
                            ),
                            FollowableAuthor(
                                author = Author(
                                    id = "1",
                                    name = "Android Dev 2",
                                    imageUrl = "",
                                    twitter = "",
                                    mediumPage = "",
                                    bio = "",
                                ),
                                isFollowed = false
                            ),
                        ),
                    ),
                    feedState = ForYouFeedState.Success(
                        feed = emptyList()
                    ),
                    onAuthorCheckedChanged = { _, _ -> },
                    onTopicCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        composeTestRule
            .onNodeWithText("Headlines")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("UI")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("Tools")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("Android Dev")
            .assertIsDisplayed()
            .assertIsOff()
            .assertHasClickAction()

        // Scroll until the Done button is visible
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(doneButtonMatcher)

        composeTestRule
            .onNode(doneButtonMatcher)
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
    }

    @Test
    fun topicSelector_whenSomeAuthorsSelected_showsTopicChipsAndEnabledDoneButton() {
        composeTestRule.setContent {
            BoxWithConstraints {
                ForYouScreen(
                    windowSizeClass = WindowSizeClass.calculateFromSize(
                        DpSize(maxWidth, maxHeight)
                    ),
                    interestsSelectionState = ForYouInterestsSelectionState.WithInterestsSelection(
                        topics = listOf(
                            FollowableTopic(
                                topic = Topic(
                                    id = "0",
                                    name = "Headlines",
                                    shortDescription = "",
                                    longDescription = "",
                                    url = "",
                                    imageUrl = ""
                                ),
                                isFollowed = false
                            ),
                            FollowableTopic(
                                topic = Topic(
                                    id = "1",
                                    name = "UI",
                                    shortDescription = "",
                                    longDescription = "",
                                    url = "",
                                    imageUrl = ""
                                ),
                                isFollowed = true
                            ),
                            FollowableTopic(
                                topic = Topic(
                                    id = "2",
                                    name = "Tools",
                                    shortDescription = "",
                                    longDescription = "",
                                    url = "",
                                    imageUrl = ""
                                ),
                                isFollowed = false
                            ),
                        ),
                        authors = listOf(
                            FollowableAuthor(
                                author = Author(
                                    id = "0",
                                    name = "Android Dev",
                                    imageUrl = "",
                                    twitter = "",
                                    mediumPage = "",
                                    bio = "",
                                ),
                                isFollowed = false
                            ),
                            FollowableAuthor(
                                author = Author(
                                    id = "1",
                                    name = "Android Dev 2",
                                    imageUrl = "",
                                    twitter = "",
                                    mediumPage = "",
                                    bio = "",
                                ),
                                isFollowed = false
                            ),
                        ),
                    ),
                    feedState = ForYouFeedState.Success(
                        feed = emptyList()
                    ),
                    onAuthorCheckedChanged = { _, _ -> },
                    onTopicCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        composeTestRule
            .onNodeWithText("Headlines")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("UI")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("Tools")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("Android Dev")
            .assertIsDisplayed()
            .assertIsOff()
            .assertHasClickAction()

        // Scroll until the Done button is visible
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(doneButtonMatcher)

        composeTestRule
            .onNode(doneButtonMatcher)
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
    }

    @Test
    fun feed_whenInterestsSelectedAndLoading_showsLoadingIndicator() {
        composeTestRule.setContent {
            BoxWithConstraints {
                ForYouScreen(
                    windowSizeClass = WindowSizeClass.calculateFromSize(
                        DpSize(maxWidth, maxHeight)
                    ),
                    interestsSelectionState = ForYouInterestsSelectionState.WithInterestsSelection(
                        topics = listOf(
                            FollowableTopic(
                                topic = Topic(
                                    id = "0",
                                    name = "Headlines",
                                    shortDescription = "",
                                    longDescription = "",
                                    url = "",
                                    imageUrl = ""
                                ),
                                isFollowed = false
                            ),
                            FollowableTopic(
                                topic = Topic(
                                    id = "1",
                                    name = "UI",
                                    shortDescription = "",
                                    longDescription = "",
                                    url = "",
                                    imageUrl = ""
                                ),
                                isFollowed = false
                            ),
                            FollowableTopic(
                                topic = Topic(
                                    id = "2",
                                    name = "Tools",
                                    shortDescription = "",
                                    longDescription = "",
                                    url = "",
                                    imageUrl = ""
                                ),
                                isFollowed = false
                            ),
                        ),
                        authors = listOf(
                            FollowableAuthor(
                                author = Author(
                                    id = "0",
                                    name = "Android Dev",
                                    imageUrl = "",
                                    twitter = "",
                                    mediumPage = "",
                                    bio = "",
                                ),
                                isFollowed = true
                            ),
                            FollowableAuthor(
                                author = Author(
                                    id = "1",
                                    name = "Android Dev 2",
                                    imageUrl = "",
                                    twitter = "",
                                    mediumPage = "",
                                    bio = "",
                                ),
                                isFollowed = false
                            ),
                        ),
                    ),
                    feedState = ForYouFeedState.Loading,
                    onAuthorCheckedChanged = { _, _ -> },
                    onTopicCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        // Scroll until the loading indicator is visible
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(
                hasContentDescription(
                    composeTestRule.activity.resources.getString(R.string.for_you_loading)
                )
            )

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.for_you_loading)
            )
            .assertExists()
    }

    @Test
    fun feed_whenNoInterestsSelectionAndLoading_showsLoadingIndicator() {
        composeTestRule.setContent {
            BoxWithConstraints {
                ForYouScreen(
                    windowSizeClass = WindowSizeClass.calculateFromSize(
                        DpSize(maxWidth, maxHeight)
                    ),
                    interestsSelectionState = ForYouInterestsSelectionState.NoInterestsSelection,
                    feedState = ForYouFeedState.Loading,
                    onAuthorCheckedChanged = { _, _ -> },
                    onTopicCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        // Scroll until the loading indicator is visible
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(
                hasContentDescription(
                    composeTestRule.activity.resources.getString(R.string.for_you_loading)
                )
            )

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.for_you_loading)
            )
            .assertExists()
    }

    @Test
    fun feed_whenNoInterestsSelectionAndLoaded_showsFeed() {
        lateinit var windowSizeClass: WindowSizeClass

        val saveableNewsResources = listOf(
            SaveableNewsResource(
                newsResource = NewsResource(
                    id = "1",
                    episodeId = "52",
                    title = "Thanks for helping us reach 1M YouTube Subscribers",
                    content = "Thank you everyone for following the Now in Android series " +
                            "and everything the Android Developers YouTube channel has to offer. " +
                            "During the Android Developer Summit, our YouTube channel reached 1 " +
                            "million subscribers! Here’s a small video to thank you all.",
                    url = "https://youtu.be/-fJ6poHQrjM",
                    headerImageUrl = "https://i.ytimg.com/vi/-fJ6poHQrjM/maxresdefault.jpg",
                    publishDate = Instant.parse("2021-11-09T00:00:00.000Z"),
                    type = NewsResourceType.Video,
                    topics = listOf(
                        Topic(
                            id = "0",
                            name = "Headlines",
                            shortDescription = "",
                            longDescription = "",
                            url = "",
                            imageUrl = ""
                        )
                    ),
                    authors = emptyList()
                ),
                isSaved = false
            ),
            SaveableNewsResource(
                newsResource = NewsResource(
                    id = "2",
                    episodeId = "52",
                    title = "Transformations and customisations in the Paging Library",
                    content = "A demonstration of different operations that can be performed " +
                            "with Paging. Transformations like inserting separators, when to " +
                            "create a new pager, and customisation options for consuming " +
                            "PagingData.",
                    url = "https://youtu.be/ZARz0pjm5YM",
                    headerImageUrl = "https://i.ytimg.com/vi/ZARz0pjm5YM/maxresdefault.jpg",
                    publishDate = Instant.parse("2021-11-01T00:00:00.000Z"),
                    type = NewsResourceType.Video,
                    topics = listOf(
                        Topic(
                            id = "1",
                            name = "UI",
                            shortDescription = "",
                            longDescription = "",
                            url = "",
                            imageUrl = ""
                        ),
                    ),
                    authors = emptyList()
                ),
                isSaved = false
            ),
            SaveableNewsResource(
                newsResource = NewsResource(
                    id = "3",
                    episodeId = "52",
                    title = "Community tip on Paging",
                    content = "Tips for using the Paging library from the developer community",
                    url = "https://youtu.be/r5JgIyS3t3s",
                    headerImageUrl = "https://i.ytimg.com/vi/r5JgIyS3t3s/maxresdefault.jpg",
                    publishDate = Instant.parse("2021-11-08T00:00:00.000Z"),
                    type = NewsResourceType.Video,
                    topics = listOf(
                        Topic(
                            id = "1",
                            name = "UI",
                            shortDescription = "",
                            longDescription = "",
                            url = "",
                            imageUrl = ""
                        ),
                    ),
                    authors = emptyList()
                ),
                isSaved = false
            ),
        )

        composeTestRule.setContent {
            BoxWithConstraints {
                windowSizeClass = WindowSizeClass.calculateFromSize(
                    DpSize(maxWidth, maxHeight)
                )

                ForYouScreen(
                    windowSizeClass = windowSizeClass,
                    interestsSelectionState = ForYouInterestsSelectionState.NoInterestsSelection,
                    feedState = ForYouFeedState.Success(
                        feed = saveableNewsResources
                    ),
                    onAuthorCheckedChanged = { _, _ -> },
                    onTopicCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        // Scroll until the second feed item is visible
        // This will cause both the first and second feed items to be visible at the same time,
        // so we can compare their positions to each other.
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(
                hasText(
                    "Transformations and customisations in the Paging Library",
                    substring = true
                )
            )

        val firstFeedItem = composeTestRule
            .onNodeWithText("Thanks for helping us reach 1M YouTube Subscribers", substring = true)
            .assertHasClickAction()
            .fetchSemanticsNode()

        val secondFeedItem = composeTestRule
            .onNodeWithText(
                "Transformations and customisations in the Paging Library",
                substring = true
            )
            .assertHasClickAction()
            .fetchSemanticsNode()

        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact, Companion.Medium -> {
                // On smaller screen widths, the second feed item should be below the first because
                // they are displayed in a single column
                assertTrue(
                    firstFeedItem.positionInRoot.y < secondFeedItem.positionInRoot.y
                )
            }

            else -> {
                // On larger screen widths, the second feed item should be inline with the first
                // because they are displayed in more than one column
                assertTrue(
                    firstFeedItem.positionInRoot.y == secondFeedItem.positionInRoot.y
                )
            }
        }
    }
}
