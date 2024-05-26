package com.balex.fbnewsclient.domain.usecases

import com.balex.fbnewsclient.domain.entity.FeedPost
import com.balex.fbnewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class ChangeFavouritePostStatusUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {

    suspend operator fun invoke(feedPost: FeedPost) {
        repository.changeFavouritePostStatus(feedPost)
    }
}