package com.balex.fbnewsclient.data.repository

import android.app.Activity
import com.balex.fbnewsclient.data.mapper.NewsFeedMapper
import com.balex.fbnewsclient.data.model.PostsDto
import com.balex.fbnewsclient.data.network.ApiFactory
import com.balex.fbnewsclient.domain.entity.AuthState
import com.balex.fbnewsclient.domain.entity.FeedPost
import com.balex.fbnewsclient.domain.entity.PostComment
import com.balex.fbnewsclient.domain.entity.StatisticItem
import com.balex.fbnewsclient.domain.entity.StatisticType
import com.balex.fbnewsclient.domain.repository.NewsFeedRepository
import com.balex.fbnewsclient.extensions.mergeWith
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import java.util.Collections

class NewsFeedRepositoryImpl : NewsFeedRepository {

    private var nextPageUrl = ""
    private val apiService = ApiFactory.apiService
    private val mapper = NewsFeedMapper()

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val nextDataPageLoaded = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    val checkAuthStateEventsToken = MutableSharedFlow<Activity>(replay = 1)


    private val authStateFlow = flow {


        //checkAuthStateEvents.emit(Unit)
        checkAuthStateEventsToken.collect {
            val accessToken = AccessToken.getCurrentAccessToken()
            var isTokenExpired = true
            accessToken?.let {accessToken ->
                isTokenExpired = accessToken.isExpired }
            if (!isTokenExpired) {
                LoginManager.getInstance().logInWithReadPermissions(it, listOf("public_profile", "user_friends"))
                emit(AuthState.Authorized)
            } else {
                emit(AuthState.NotAuthorized)
            }

        }

    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )


    private val loadedListFlow: Flow<List<FeedPost>> = flow {
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
            emit(userPosts)
            nextDataPageLoaded.collect {
                emit(feedPosts)
            }
        } else {
            throw RuntimeException("User profile is empty, can't get user.id")
        }
    }.retry {
    //}.retry(2) {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }

    private val repositoryPosts: StateFlow<List<FeedPost>> = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )


    override fun getAuthStateFlow(): StateFlow<AuthState> = authStateFlow

    override fun getRepositoryPosts(): StateFlow<List<FeedPost>> = repositoryPosts



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



    override suspend fun getNextPage() {

            if (nextPageUrl.length > MIN_LENGTH_STRING_NEXT_QUERY) {
                val deferredUserPosts = CoroutineScope(Dispatchers.IO).async {
                    apiService.getNextPageUserPosts(
                        cutNextUrl(nextPageUrl)
                    )
                }
                val response = deferredUserPosts.await()
                nextPageUrl = response.paging.next
                _feedPosts.addAll(mapper.mapResponseToPosts(response))
                nextDataPageLoaded.emit(Unit)
            }
    }


    override suspend fun changeLikeStatus(feedPost: FeedPost) {
        //throw RuntimeException()
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
        refreshedListFlow.emit(feedPosts)
    }

    override suspend fun deletePost(feedPost: FeedPost) {
        _feedPosts.remove(feedPost)
        refreshedListFlow.emit(feedPosts)
    }

    override fun getComments(): StateFlow<List<PostComment>> = flow {
        val comments = mutableListOf<PostComment>().apply {
            repeat(10) {
                add(PostComment(id = it))
            }
        }
        emit(comments)

    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )


    companion object {
        private const val NUMBER_PAGE_TO_LOAD = 2
        private const val MIN_LENGTH_STRING_NEXT_QUERY = 30
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}

