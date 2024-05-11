package com.balex.fbnewsclient.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult


class MainViewModel() : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Initial)
    val authState: LiveData<AuthState> = _authState



    fun processSuccessLoginResult() {
        _authState.value = AuthState.Authorized
    }


     fun checkToken(activity: MainActivity) {
        val accessToken = AccessToken.getCurrentAccessToken()
        var isTokenExpired = true
        accessToken?.let { isTokenExpired = it.isExpired }
        if (!isTokenExpired) {
            LoginManager.getInstance().logInWithReadPermissions(activity, listOf("public_profile", "user_friends"))
            _authState.value = AuthState.Authorized
        } else {
            _authState.value = AuthState.NotAuthorized
        }
    }

}