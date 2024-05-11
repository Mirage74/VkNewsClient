package com.balex.fbnewsclient.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balex.fbnewsclient.data.mapper.NewsFeedMapper
import com.balex.fbnewsclient.data.network.ApiFactory
import com.balex.fbnewsclient.domain.FeedPost
import com.balex.fbnewsclient.domain.StatisticItem
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NewsFeedViewModel : ViewModel() {



    private val _screenState = MutableLiveData<NewsFeedScreenState>(NewsFeedScreenState.Initial())
    val screenState: LiveData<NewsFeedScreenState> = _screenState

    private val _userFacebookProfile = MutableLiveData<UserProfileState> (UserProfileState.Initial())
    val userFacebookProfile: LiveData<UserProfileState> = _userFacebookProfile

    private val mapper = NewsFeedMapper()

    init {
        viewModelScope.launch {
            getUserProfile()
        }

    }

    private suspend fun getUserProfile() {
        val deferredUserFacebookProfile = viewModelScope.async {
            ApiFactory.apiService.getUserProfile()
        }
        val userFacebookProfile = deferredUserFacebookProfile.await()
        _userFacebookProfile.value = UserProfileState.User(userFacebookProfile)
        if (userFacebookProfile.id.isNotBlank()) {
            getUserPosts(userFacebookProfile.id)
        } else {
            throw RuntimeException("User profile is empty, can't get user.id")
        }
    }

    private suspend fun getUserPosts(id: String) {
        val deferredUserPosts = viewModelScope.async {
            ApiFactory.apiService.getUserPosts(id)
        }
        val response = deferredUserPosts.await()
        val feedPosts = mapper.mapResponseToPosts(response)
        _screenState.value = NewsFeedScreenState.Posts(posts = feedPosts)

    }

    fun updateCount(feedPost: FeedPost, item: StatisticItem) {
        val currentState = screenState.value
        if (currentState !is NewsFeedScreenState.Posts) return

        val oldPosts = currentState.posts.toMutableList()
        val oldStatistics = feedPost.statistics
        val newStatistics = oldStatistics.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == item.type) {
                    oldItem.copy(count = oldItem.count + 1)
                } else {
                    oldItem
                }
            }
        }
        val newFeedPost = feedPost.copy(statistics = newStatistics)
        val newPosts = oldPosts.apply {
            replaceAll {
                if (it.id == newFeedPost.id) {
                    newFeedPost
                } else {
                    it
                }
            }
        }
        _screenState.value = NewsFeedScreenState.Posts(posts = newPosts)
    }

    fun remove(feedPost: FeedPost) {
        val currentState = screenState.value
        if (currentState !is NewsFeedScreenState.Posts) return

        val oldPosts = currentState.posts.toMutableList()
        oldPosts.remove(feedPost)
        _screenState.value = NewsFeedScreenState.Posts(posts = oldPosts)
    }
}