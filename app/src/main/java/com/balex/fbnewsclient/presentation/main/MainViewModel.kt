package com.balex.fbnewsclient.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balex.fbnewsclient.data.repository.NewsFeedRepository
import com.balex.fbnewsclient.domain.AuthState
import com.balex.fbnewsclient.domain.FeedPost
import com.balex.fbnewsclient.extensions.mergeWith
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class MainViewModel() : ViewModel() {


    private val repository = NewsFeedRepository()
    private val isUserAuthorized = MutableSharedFlow<AuthState>()


    val authState = repository.authStateFlow
        .mergeWith(isUserAuthorized)


    fun processSuccessLoginResult() {
        Log.d("NewsFeedRepositoryImpl", "MainViewModel repository: $repository")
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