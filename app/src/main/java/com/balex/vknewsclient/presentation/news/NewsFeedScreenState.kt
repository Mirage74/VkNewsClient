package com.balex.vknewsclient.presentation.news

import com.balex.vknewsclient.domain.FeedPost

sealed class NewsFeedScreenState {

    data object Initial: NewsFeedScreenState()

    data class Posts(val posts: List<FeedPost>) : NewsFeedScreenState()

}

