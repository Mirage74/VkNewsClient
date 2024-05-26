package com.balex.fbnewsclient.domain.usecases

import com.balex.fbnewsclient.domain.entity.UserFacebookProfile
import com.balex.fbnewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(): StateFlow<UserFacebookProfile> {
        return repository.getUserFacebookProfile()
    }
}