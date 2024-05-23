package com.balex.fbnewsclient.domain.usecases

import com.balex.fbnewsclient.domain.entity.FeedPost
import com.balex.fbnewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetRepositoryPostsUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {

    operator fun invoke(): StateFlow<List<FeedPost>> {
        return repository.getRepositoryPosts()
    }
}