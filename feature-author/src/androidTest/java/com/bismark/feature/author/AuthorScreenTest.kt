package com.bismark.feature.author

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.bismark.core.model.Author
import com.bismark.core.model.FollowableAuthor
import com.bismark.core.model.NewsResource
import com.bismark.core.model.NewsResourceType
import kotlinx.datetime.Instant
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * UI test for checking the correct behaviour of the Author screen;
 * Verifies that, when a specific UiState is set, the corresponding
 * composables and details are shown
 */
class AuthorScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var authorLoading: String

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            authorLoading = getString(R.string.author_loading)
        }
    }

    @Test
    fun niaLoadingWheel_whenScreenIsLoading_showLoading() {
        composeTestRule.setContent {
            AuthorScreen(
                authorState = AuthorUiState.Loading,
                newsState = NewsUiState.Loading,
                onBackClick = { },
                onFollowClick = { }
            )
        }

        composeTestRule
            .onNodeWithContentDescription(authorLoading)
            .assertExists()
    }

    @Test
    fun authorTitle_whenAuthorIsSuccess_isShown() {
        val testAuthor = testAuthors.first()
        composeTestRule.setContent {
            AuthorScreen(
                authorState = AuthorUiState.Success(testAuthor),
                newsState = NewsUiState.Loading,
                onBackClick = { },
                onFollowClick = { }
            )
        }

        // Name is shown
        composeTestRule
            .onNodeWithText(testAuthor.author.name)
            .assertExists()

        // Bio is shown
        composeTestRule
            .onNodeWithText(testAuthor.author.bio)
            .assertExists()
    }

    @Test
    fun news_whenAuthorIsLoading_isNotShown() {
        composeTestRule.setContent {
            AuthorScreen(
                authorState = AuthorUiState.Loading,
                newsState = NewsUiState.Success(sampleNewsResources),
                onBackClick = { },
                onFollowClick = { }
            )
        }

        // Loading indicator shown
        composeTestRule
            .onNodeWithContentDescription(authorLoading)
            .assertExists()
    }
    @Test
    fun news_whenSuccessAndAuthorIsSuccess_isShown() {
        val testAuthor = testAuthors.first()
        composeTestRule.setContent {
            AuthorScreen(
                authorState = AuthorUiState.Success(testAuthor),
                newsState = NewsUiState.Success(sampleNewsResources),
                onBackClick = { },
                onFollowClick = { }
            )
        }

        // First news title shown
        composeTestRule
            .onNodeWithText(sampleNewsResources.first().title)
            .assertExists()
    }
}

private const val AUTHOR_1_NAME = "Author 1"
private const val AUTHOR_2_NAME = "Author 2"
private const val AUTHOR_3_NAME = "Author 3"
private const val AUTHOR_BIO = "At vero eos et accusamus et iusto odio dignissimos ducimus qui."

private val testAuthors = listOf(
    FollowableAuthor(
        Author(
            id = "0",
            name = AUTHOR_1_NAME,
            twitter = "",
            bio = AUTHOR_BIO,
            mediumPage = "",
            imageUrl = ""
        ),
        isFollowed = true
    ),
    FollowableAuthor(
        Author(
            id = "1",
            name = AUTHOR_2_NAME,
            twitter = "",
            bio = AUTHOR_BIO,
            mediumPage = "",
            imageUrl = ""
        ),
        isFollowed = false
    ),
    FollowableAuthor(
        Author(
            id = "2",
            name = AUTHOR_3_NAME,
            twitter = "",
            bio = AUTHOR_BIO,
            mediumPage = "",
            imageUrl = ""
        ),
        isFollowed = false
    )
)

private val sampleNewsResources = listOf(
    NewsResource(
        id = "1",
        episodeId = "52",
        title = "Thanks for helping us reach 1M YouTube Subscribers",
        content = "Thank you everyone for following the Now in Android series and everything the " +
            "Android Developers YouTube channel has to offer. During the Android Developer " +
            "Summit, our YouTube channel reached 1 million subscribers! Here’s a small video to " +
            "thank you all.",
        url = "https://youtu.be/-fJ6poHQrjM",
        headerImageUrl = "https://i.ytimg.com/vi/-fJ6poHQrjM/maxresdefault.jpg",
        publishDate = Instant.parse("2021-11-09T00:00:00.000Z"),
        type = NewsResourceType.Video,
        authors = listOf(
            Author(
                id = "0",
                name = "Headlines",
                twitter = "",
                bio = AUTHOR_BIO,
                mediumPage = "",
                imageUrl = ""
            )
        ),
        topics = emptyList()
    )
)
