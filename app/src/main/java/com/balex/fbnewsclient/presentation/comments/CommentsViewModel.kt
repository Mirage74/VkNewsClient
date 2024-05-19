package com.balex.fbnewsclient.presentation.comments

import androidx.lifecycle.ViewModel
import com.balex.fbnewsclient.data.repository.NewsFeedRepository
import com.balex.fbnewsclient.domain.FeedPost
import kotlinx.coroutines.flow.map

class CommentsViewModel(
    feedPost: FeedPost
) : ViewModel() {

    private val repository = NewsFeedRepository()

    val screenState = repository.getComments()
        .map {
            CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = it
            )
        }
    }