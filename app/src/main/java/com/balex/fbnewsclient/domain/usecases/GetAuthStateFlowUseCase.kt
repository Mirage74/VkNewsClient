package com.balex.fbnewsclient.domain.usecases

import com.balex.fbnewsclient.domain.entity.AuthState
import com.balex.fbnewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow

class GetAuthStateFlowUseCase(
    private val repository: NewsFeedRepository
) {

    operator fun invoke(): StateFlow<AuthState> {
        return repository.getAuthStateFlow()
    }
}