package com.balex.fbnewsclient.presentation.favourite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balex.fbnewsclient.domain.entity.FeedPost
import com.balex.fbnewsclient.domain.usecases.ChangeFavouritePostStatusUseCase
import com.balex.fbnewsclient.domain.usecases.ChangeLikeStatusUseCase
import com.balex.fbnewsclient.domain.usecases.GetFavouriteListUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouritesViewModel @Inject constructor(
    getFavouritesPostsUseCase: GetFavouriteListUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val changeFavouritePostStatusUseCase: ChangeFavouritePostStatusUseCase
    ) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("FavouritesViewModel", "Exception caught by exception handler in FavouritesViewModel")
    }


    private val favouritesFlow = getFavouritesPostsUseCase()

    val screenState = favouritesFlow
        //.filter { it.isNotEmpty() }
        .map { FavouritesScreenState.Posts(posts = it) as FavouritesScreenState }


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

}

