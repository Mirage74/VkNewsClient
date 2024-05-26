package com.balex.fbnewsclient.presentation.favourite

import com.balex.fbnewsclient.domain.entity.FeedPost
import java.util.Collections

sealed class FavouritesScreenState {

    data class Initial(val posts: List<FeedPost> = Collections.emptyList()) : FavouritesScreenState()

    data class Posts(val posts: List<FeedPost>) : FavouritesScreenState()

}