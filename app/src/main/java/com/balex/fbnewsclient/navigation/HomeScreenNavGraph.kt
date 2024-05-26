package com.balex.fbnewsclient.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.balex.fbnewsclient.domain.entity.FeedPost


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (FeedPost) -> Unit
) {
    navigation(
        startDestination = Screen.NewsFeed.route,
        route = Screen.Home.route
    ) {
        composable(Screen.NewsFeed.route) {
            newsFeedScreenContent()
        }
        composable(
            route = Screen.Comments.route,
            arguments = listOf(
                navArgument(Screen.KEY_FEED_POST) {
                    type = FeedPost.NavigationType
                }
            )
        ) { //comments/{feed_post_id}
            val feedPost = it.arguments?.getParcelable(
                Screen.KEY_FEED_POST,
                FeedPost::class.java
            ) ?: throw RuntimeException("Args is null")


            commentsScreenContent(feedPost)
        }
    }
}