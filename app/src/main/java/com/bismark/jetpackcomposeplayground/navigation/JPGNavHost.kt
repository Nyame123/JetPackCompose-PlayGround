package com.bismark.jetpackcomposeplayground.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bismark.feature.author.navigation.AuthorDestination
import com.bismark.feature.author.navigation.authorGraph
import com.bismark.feature.foryou.navigation.ForYouDestination
import com.bismark.feature.foryou.navigation.forYouGraph
import com.bismark.feature.interests.navigation.interestsGraph
import com.bismark.feature.topic.navigation.TopicDestination
import com.bismark.feature.topic.navigation.topicGraph

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun JPGNavHost(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ForYouDestination.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        forYouGraph(
            windowSizeClass = windowSizeClass
        )
        interestsGraph(
            navigateToTopic = { navController.navigate("${TopicDestination.route}/$it") },
            navigateToAuthor = { navController.navigate("${AuthorDestination.route}/$it") }
        )
        topicGraph(
            onBackClick = { navController.popBackStack() }
        )
        authorGraph(
            onBackClick = { navController.popBackStack() }
        )
    }
}
