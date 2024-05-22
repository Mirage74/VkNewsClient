package com.balex.fbnewsclient.domain.usecases

import com.balex.fbnewsclient.domain.repository.NewsFeedRepository

class LoadNextDataUseCase(
    private val repository: NewsFeedRepository
) {

    suspend operator fun invoke() {
        repository.getNextPage()
    }
}