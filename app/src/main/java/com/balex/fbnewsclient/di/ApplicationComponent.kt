package com.balex.fbnewsclient.di

import android.content.Context
import com.balex.fbnewsclient.domain.entity.FeedPost
import com.balex.fbnewsclient.presentation.main.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            @BindsInstance feedPost: FeedPost
        ): ApplicationComponent
    }
}