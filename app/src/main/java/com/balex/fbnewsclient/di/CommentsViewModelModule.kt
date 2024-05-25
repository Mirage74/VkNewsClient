package com.balex.fbnewsclient.di

import androidx.lifecycle.ViewModel
import com.balex.fbnewsclient.presentation.comments.CommentsViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface CommentsViewModelModule {

    @IntoMap
    @ViewModelKey(CommentsViewModel::class)
    @Binds
    fun bindCommentsViewModel(viewModel: CommentsViewModel): ViewModel
}