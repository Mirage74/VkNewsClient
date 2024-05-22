package com.balex.fbnewsclient.domain.repository

import com.balex.fbnewsclient.domain.entity.AuthState
import com.balex.fbnewsclient.domain.entity.FeedPost
import com.balex.fbnewsclient.domain.entity.PostComment
import kotlinx.coroutines.flow.StateFlow

interface NewsFeedRepository {

    fun getAuthStateFlow(): StateFlow<AuthState>

    fun getRepositoryPosts(): StateFlow<List<FeedPost>>

    fun getComments(): StateFlow<List<PostComment>>

    suspend fun getNextPage()

    suspend fun deletePost(feedPost: FeedPost)

    suspend fun changeLikeStatus(feedPost: FeedPost)


}