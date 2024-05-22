package com.balex.fbnewsclient.domain.usecases

import com.balex.fbnewsclient.domain.entity.FeedPost
import com.balex.fbnewsclient.domain.repository.NewsFeedRepository

class DeletePostUseCase(
    private val repository: NewsFeedRepository
) {

    suspend operator fun invoke(feedPost: FeedPost) {
        repository.deletePost(feedPost)
    }
}