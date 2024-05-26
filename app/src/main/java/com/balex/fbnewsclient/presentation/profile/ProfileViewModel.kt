package com.balex.fbnewsclient.presentation.profile

import androidx.lifecycle.ViewModel
import com.balex.fbnewsclient.domain.usecases.GetProfileUseCase
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val userProfileFlow = getProfileUseCase()

    val screenState = userProfileFlow
        .filter { it.id.isNotEmpty() }
        .map { UserProfileState.User(user = it) as UserProfileState }

}