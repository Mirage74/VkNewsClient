package com.balex.fbnewsclient.presentation.profile

import com.balex.fbnewsclient.domain.entity.UserFacebookProfile


sealed class UserProfileState {

    data class Initial(val user: UserFacebookProfile = UserFacebookProfile()) : UserProfileState()

    data class User(val user: UserFacebookProfile) : UserProfileState()

}