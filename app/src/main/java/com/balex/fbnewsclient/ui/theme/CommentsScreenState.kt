package com.balex.fbnewsclient.ui.theme

import com.balex.fbnewsclient.domain.FeedPost
import com.balex.fbnewsclient.domain.PostComment

sealed class CommentsScreenState {

    data object Initial : CommentsScreenState()

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState()
}