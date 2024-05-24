package com.balex.fbnewsclient.presentation

import android.app.Application
import com.balex.fbnewsclient.di.ApplicationComponent
import com.balex.fbnewsclient.di.DaggerApplicationComponent
import com.balex.fbnewsclient.domain.entity.FeedPost


class NewsFeedApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(
            this,
            FeedPost(id = "", publicationDate = "", contentText = "")
        )
    }
}