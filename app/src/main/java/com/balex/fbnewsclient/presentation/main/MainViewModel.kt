package com.balex.fbnewsclient.presentation.main

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balex.fbnewsclient.domain.entity.AuthState
import com.balex.fbnewsclient.extensions.mergeWith
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(

) : ViewModel() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val isUserAuthorized = MutableSharedFlow<AuthState>()


    private val checkAuthStateEventsToken = MutableSharedFlow<Activity>(replay = 1)


    private val authStateFlow = flow {
        checkAuthStateEventsToken.collect {
            val accessToken = AccessToken.getCurrentAccessToken()
            var isTokenExpired = true
            accessToken?.let { accessToken ->
                isTokenExpired = accessToken.isExpired
            }
            if (!isTokenExpired) {
                LoginManager.getInstance()
                    .logInWithReadPermissions(it, listOf("public_profile", "user_friends"))
                emit(AuthState.Authorized)
            } else {
                emit(AuthState.NotAuthorized)
            }

        }

    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )


    val authState = authStateFlow
        .mergeWith(isUserAuthorized)


    fun processSuccessLoginResult() {
        viewModelScope.launch {
            isUserAuthorized.emit(AuthState.Authorized)
        }
    }

    fun checkToken(activity: Activity) {
        viewModelScope.launch {
            checkAuthStateEventsToken.emit(activity)
        }
    }

}