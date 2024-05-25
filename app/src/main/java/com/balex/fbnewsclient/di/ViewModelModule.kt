package com.balex.fbnewsclient.di

import androidx.lifecycle.ViewModel
import com.balex.fbnewsclient.presentation.comments.CommentsViewModel
import com.balex.fbnewsclient.presentation.main.MainViewModel
import com.balex.fbnewsclient.presentation.news.NewsFeedViewModel
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

}