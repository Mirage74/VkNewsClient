package com.balex.fbnewsclient.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balex.fbnewsclient.data.mapper.NewsFeedMapper
import com.balex.fbnewsclient.data.model.PostsDto
import com.balex.fbnewsclient.data.network.ApiFactory
import com.balex.fbnewsclient.data.repository.NewsFeedRepository
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

    private val mapper = NewsFeedMapper()

    private val repository = NewsFeedRepository()

    init {
        loadFeed()

    }

    private fun loadFeed() {
        viewModelScope.launch {
            val feedPosts = repository.loadProfileAndFeed()
            _screenState.value = NewsFeedScreenState.Posts(posts = feedPosts)
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        repository.changeLikeStatus(feedPost)
        _screenState.value = NewsFeedScreenState.Posts(posts = repository.feedPosts)

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

