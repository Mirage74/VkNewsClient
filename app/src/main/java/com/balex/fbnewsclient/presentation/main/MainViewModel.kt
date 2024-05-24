package com.balex.fbnewsclient.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balex.fbnewsclient.data.repository.NewsFeedRepositoryImpl
import com.balex.fbnewsclient.domain.entity.AuthState
import com.balex.fbnewsclient.domain.usecases.GetAuthStateFlowUseCase
import com.balex.fbnewsclient.extensions.mergeWith
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getAuthStateFlowUseCase: GetAuthStateFlowUseCase,
    private val repository: NewsFeedRepositoryImpl
) : ViewModel() {

    private val isUserAuthorized = MutableSharedFlow<AuthState>()

    val authState = getAuthStateFlowUseCase()
        .mergeWith(isUserAuthorized)

    fun processSuccessLoginResult() {
        viewModelScope.launch {
            isUserAuthorized.emit(AuthState.Authorized)
        }
    }

    fun checkToken(activity: MainActivity) {
        viewModelScope.launch {
            repository.checkAuthStateEventsToken.emit(activity)
        }
    }

}