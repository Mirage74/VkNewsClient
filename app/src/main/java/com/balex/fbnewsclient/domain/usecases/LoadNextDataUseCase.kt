package com.balex.fbnewsclient.domain.usecases

import com.balex.fbnewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class LoadNextDataUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {

    suspend operator fun invoke() {
        repository.getNextPage()
    }
}