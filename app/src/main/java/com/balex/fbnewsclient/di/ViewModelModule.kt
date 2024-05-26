package com.balex.fbnewsclient.di

import androidx.lifecycle.ViewModel
import com.balex.fbnewsclient.presentation.favourite.FavouritesViewModel
import com.balex.fbnewsclient.presentation.main.MainViewModel
import com.balex.fbnewsclient.presentation.news.NewsFeedViewModel
import com.balex.fbnewsclient.presentation.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(NewsFeedViewModel::class)
    @Binds
    fun bindNewsFeedViewModel(viewModel: NewsFeedViewModel): ViewModel

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    @Binds
    fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @IntoMap
    @ViewModelKey(FavouritesViewModel::class)
    @Binds
    fun bindFavouritesViewModel(viewModel: FavouritesViewModel): ViewModel

}