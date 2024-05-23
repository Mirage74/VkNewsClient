package com.balex.fbnewsclient.di

import com.balex.fbnewsclient.data.repository.NewsFeedRepositoryImpl
import com.balex.fbnewsclient.domain.repository.NewsFeedRepository
import dagger.Binds
import dagger.Module


//4:50
@Module
interface DataModule {
    @Binds
    fun bindRepository(impl: NewsFeedRepositoryImpl): NewsFeedRepository
}