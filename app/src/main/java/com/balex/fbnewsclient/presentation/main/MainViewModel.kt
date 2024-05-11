package com.balex.fbnewsclient.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balex.fbnewsclient.data.model.PostsDto
import com.balex.fbnewsclient.data.network.ApiFactory
import com.balex.fbnewsclient.domain.UserFacebookProfile
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainViewModel(private val activity: MainActivity) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Initial)
    val authState: LiveData<AuthState> = _authState

    private val _userFacebookProfile = MutableLiveData(UserFacebookProfile())
    val userFacebookProfile: LiveData<UserFacebookProfile> = _userFacebookProfile

    private val _userFacebookPosts = MutableLiveData<PostsDto>()
    val userFacebookPosts: MutableLiveData<PostsDto> = _userFacebookPosts

    init {
        checkToken()
        viewModelScope.launch {
            getUserProfile()
        }

    }

    private suspend fun getUserProfile() {
        val deferredUserFacebookProfile = viewModelScope.async {
            ApiFactory.apiService.getUserProfile()
        }
        val userFacebookProfile = deferredUserFacebookProfile.await()
        _userFacebookProfile.value = userFacebookProfile

    }

    suspend fun getUserPosts() {
        val deferredUserPosts = viewModelScope.async {
            var id = ""
            _userFacebookProfile.value?.let { id = it.id}
            ApiFactory.apiService.getUserPosts(id)
        }
        val userFacebookPosts = deferredUserPosts.await()
        _userFacebookPosts.value = userFacebookPosts

    }

    fun processSuccessLoginResult(result: LoginResult) {
        _authState.value = AuthState.Authorized
    }

    fun setUserFacebookProfile(userFacebookProfile: UserFacebookProfile) {
        _userFacebookProfile.value = userFacebookProfile
    }

    private fun checkToken() {
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