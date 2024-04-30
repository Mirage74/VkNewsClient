package com.balex.vknewsclient.ui.theme

import com.balex.vknewsclient.domain.FeedPost
import com.balex.vknewsclient.domain.PostComment

sealed class HomeScreenState {

    data object Initial: HomeScreenState()

    data class Posts(val posts: List<FeedPost>) : HomeScreenState()

    data class Comments(val feedPost: FeedPost, val comments: List<PostComment>) : HomeScreenState()
}
