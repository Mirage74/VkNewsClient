package com.balex.fbnewsclient.presentation.comments

import com.balex.fbnewsclient.domain.entity.FeedPost
import com.balex.fbnewsclient.domain.entity.PostComment

sealed class CommentsScreenState {

    data object Initial : CommentsScreenState()

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState()
}