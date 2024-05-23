package com.balex.fbnewsclient.presentation.comments

import androidx.lifecycle.ViewModel
import com.balex.fbnewsclient.data.repository.NewsFeedRepositoryImpl
import com.balex.fbnewsclient.domain.entity.FeedPost
import com.balex.fbnewsclient.domain.usecases.GetCommentsUseCase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommentsViewModel @Inject constructor (
    feedPost: FeedPost
) : ViewModel() {

    private val repository = NewsFeedRepositoryImpl()

    private val getCommentsUseCase = GetCommentsUseCase(repository)

    val screenState = getCommentsUseCase()
        .map {
            CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = it
            )
        }
    }