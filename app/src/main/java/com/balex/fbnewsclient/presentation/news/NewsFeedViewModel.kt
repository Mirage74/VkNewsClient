package com.balex.fbnewsclient.presentation.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balex.fbnewsclient.domain.entity.FeedPost
import com.balex.fbnewsclient.domain.usecases.ChangeFavouritePostStatusUseCase
import com.balex.fbnewsclient.domain.usecases.ChangeLikeStatusUseCase
import com.balex.fbnewsclient.domain.usecases.ChangeLikeStatusUseCase_Factory
import com.balex.fbnewsclient.domain.usecases.DeletePostUseCase
import com.balex.fbnewsclient.domain.usecases.GetRepositoryPostsUseCase
import com.balex.fbnewsclient.domain.usecases.LoadNextDataUseCase
import com.balex.fbnewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsFeedViewModel @Inject constructor(
    getPostsUseCase: GetRepositoryPostsUseCase,
    private val loadNextDataUseCase: LoadNextDataUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val changeFavouritePostStatusUseCase: ChangeFavouritePostStatusUseCase,
    private val deletePostUseCase: DeletePostUseCase,

) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("NewsFeedViewModel", "Exception caught by exception handler")
    }


    private val feedsFlow = getPostsUseCase()

    private val loadNextDataFlow = MutableSharedFlow<NewsFeedScreenState>()


    val screenState = feedsFlow
        .filter { it.isNotEmpty() }
        .map { NewsFeedScreenState.Posts(posts = it) as NewsFeedScreenState }
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(loadNextDataFlow)

    fun loadNextPageFeed() {
        viewModelScope.launch {
            loadNextDataFlow.emit(
                NewsFeedScreenState.Posts(
                    posts = feedsFlow.value,
                    nextDataIsLoading = true
                )
            )
            loadNextDataUseCase()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            changeLikeStatusUseCase(feedPost)
        }
    }

    fun changeFavouriteStatus(feedPost: FeedPost) {
            viewModelScope.launch(exceptionHandler) {
                changeFavouritePostStatusUseCase(feedPost)
            }
    }


    fun remove(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            deletePostUseCase(feedPost)
        }
    }


}

