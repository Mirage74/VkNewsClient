package com.balex.fbnewsclient.data.repository

import com.balex.fbnewsclient.data.mapper.NewsFeedMapper
import com.balex.fbnewsclient.data.model.PostsDto
import com.balex.fbnewsclient.data.network.ApiFactory
import com.balex.fbnewsclient.domain.FeedPost
import com.balex.fbnewsclient.domain.StatisticItem
import com.balex.fbnewsclient.domain.StatisticType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.Collections

class NewsFeedRepository() {

    private var nextPageUrl = ""

    private val apiService = ApiFactory.apiService

    private val mapper = NewsFeedMapper()

    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()


    suspend fun loadProfileAndFeed(): List<FeedPost> {
        val deferredUserFacebookProfile = CoroutineScope(Dispatchers.IO).async {
            ApiFactory.apiService.getUserProfile()
        }
        val userFacebookProfile = deferredUserFacebookProfile.await()
        if (userFacebookProfile.id.isNotBlank()) {
            val userPosts = mutableListOf<FeedPost>()
            for (i in 1..NUMBER_PAGE_TO_LOAD) {
                userPosts.addAll(getUserPosts(userFacebookProfile.id, i))
            }
            _feedPosts.addAll(userPosts)
            return userPosts
        } else {
            throw RuntimeException("User profile is empty, can't get user.id")
        }
    }


    private suspend fun getUserPosts(id: String, numPage: Int): List<FeedPost> {
        if ((numPage > 1) && (nextPageUrl.length < MIN_LENGTH_STRING_NEXT_QUERY)) {
            return Collections.emptyList()
        } else {
            val deferredUserPosts: Deferred<PostsDto> = if (numPage == 1) {
                CoroutineScope(Dispatchers.IO).async { apiService.getUserPosts(id) }
            } else {
                CoroutineScope(Dispatchers.IO).async {
                    apiService.getNextPageUserPosts(
                        cutNextUrl(
                            nextPageUrl
                        )
                    )
                }
            }

            val response = deferredUserPosts.await()
            nextPageUrl = response.paging.next

            return mapper.mapResponseToPosts(response)
        }
    }

    private fun cutNextUrl(oldUrl: String): String {
        val maskSearch = "facebook.com/"
        var indexEnd = oldUrl.indexOf(maskSearch) + maskSearch.length
        var tempStr = oldUrl.substring(indexEnd)
        indexEnd = tempStr.indexOf("/") + 1
        tempStr = tempStr.substring(indexEnd)
        return tempStr
    }

    suspend fun getNextPage(): List<FeedPost> {
        if (nextPageUrl.length > MIN_LENGTH_STRING_NEXT_QUERY) {
            val deferredUserPosts = CoroutineScope(Dispatchers.IO).async {
                apiService.getNextPageUserPosts(
                    cutNextUrl(nextPageUrl)
                )
            }
            val response = deferredUserPosts.await()
            nextPageUrl = response.paging.next
            _feedPosts.addAll(mapper.mapResponseToPosts(response))
        }
        return feedPosts
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        val likeItem = feedPost.statistics.first {
            it.type == StatisticType.LIKES
        }
        var newNumLikes = likeItem.count
        if (feedPost.isLiked) {
            newNumLikes--

        } else {
            newNumLikes++
        }

        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(type = StatisticType.LIKES, newNumLikes))
        }
        val newPost = feedPost.copy(statistics = newStatistics, isLiked = !feedPost.isLiked)
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
    }


    companion object {
        const val NUMBER_PAGE_TO_LOAD = 1
        const val MIN_LENGTH_STRING_NEXT_QUERY = 30
    }
}