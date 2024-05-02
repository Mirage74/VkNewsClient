package com.balex.vknewsclient.ui.theme

import com.balex.vknewsclient.domain.FeedPost
import com.balex.vknewsclient.domain.PostComment

sealed class CommentsScreenState {

    data object Initial : CommentsScreenState()

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState()
}