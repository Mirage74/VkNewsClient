package com.balex.fbnewsclient.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balex.fbnewsclient.data.mapper.NewsFeedMapper
import com.balex.fbnewsclient.data.model.PostsDto
import com.balex.fbnewsclient.data.network.ApiFactory
import com.balex.fbnewsclient.domain.FeedPost
import com.balex.fbnewsclient.domain.StatisticItem
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.util.Collections

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
            getUserPosts(userFacebookProfile.id, 1, "")
        } else {
            throw RuntimeException("User profile is empty, can't get user.id")
        }
    }

    private fun curNextUrl(oldUrl: String): String {
        val maskSearch = "facebook.com/"
        var indexEnd = oldUrl.indexOf(maskSearch) + maskSearch.length
        var tempStr = oldUrl.substring(indexEnd)
        indexEnd = tempStr.indexOf("/") + 1
        tempStr = tempStr.substring(indexEnd)
        val u = URLEncoder.encode(tempStr, "utf-8")
        //return URLEncoder.encode(tempStr, "utf-8")
        return tempStr
    }

    private suspend fun getUserPosts(id: String, numPage: Int, nextUrl: String) {
        val deferredUserPosts: Deferred<PostsDto> = if (numPage == 1) {
            viewModelScope.async {
                ApiFactory.apiService.getUserPosts(id)
            }
        } else {
            viewModelScope.async {
                ApiFactory.apiService.getNextPageUserPosts(nextUrl)
            }
        }

        val response = deferredUserPosts.await()

        var feedPosts = mutableListOf<FeedPost>()
        if (_screenState.value is NewsFeedScreenState.Posts) {
            feedPosts = (_screenState.value as NewsFeedScreenState.Posts).posts.toList().toMutableList()
        }
        val feedPostsNew = mapper.mapResponseToPosts(response)

        feedPosts.addAll(feedPostsNew)

        _screenState.value = NewsFeedScreenState.Posts(posts = feedPosts)
        if ( (numPage < NUMBER_PAGE_TO_LOAD) && (response.paging.next.length > MIN_LENGTH_STRING_NEXT_QUERY)){
            getUserPosts(id, numPage + 1, curNextUrl(response.paging.next))
        }

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

    companion object {
        const val NUMBER_PAGE_TO_LOAD = 3
        const val MIN_LENGTH_STRING_NEXT_QUERY = 30
    }

}

