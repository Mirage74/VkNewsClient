package com.balex.fbnewsclient.presentation.news

import com.balex.fbnewsclient.domain.entity.FeedPost
import java.util.Collections

sealed class NewsFeedScreenState {

    data class Initial(val posts: List<FeedPost> = Collections.emptyList()) : NewsFeedScreenState()

    data object Loading : NewsFeedScreenState()

    data class Posts(
        val posts: List<FeedPost>,
        val nextDataIsLoading: Boolean = false
    ) : NewsFeedScreenState()

}

