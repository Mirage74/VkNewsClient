package com.balex.fbnewsclient.presentation.favourite

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.balex.fbnewsclient.domain.entity.FeedPost
import com.balex.fbnewsclient.presentation.getApplicationComponent
import com.balex.fbnewsclient.presentation.news.PostCard

@Composable
fun FavouritesScreen(
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit
) {
    val component = getApplicationComponent()
    val viewModel: FavouritesViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.screenState.collectAsState(FavouritesScreenState.Initial())

    FavouritesScreenContent(
        screenState = screenState,
        paddingValues = paddingValues,
        onCommentClickListener = onCommentClickListener,
        viewModel = viewModel
    )

}

@Composable
private fun FavouritesScreenContent(
    screenState: State<FavouritesScreenState>,
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit,
    viewModel: FavouritesViewModel
) {
    when (val currentScreenState = screenState.value) {
        is FavouritesScreenState.Posts -> {
            FeedPosts(
                viewModel = viewModel,
                paddingValues = paddingValues,
                posts = currentScreenState.posts,
                onCommentClickListener = onCommentClickListener
            )
        }

        is FavouritesScreenState.Initial -> {}

    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    viewModel: FavouritesViewModel,
    paddingValues: PaddingValues,
    posts: List<FeedPost>,
    onCommentClickListener: (FeedPost) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues),
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = posts,
            key = { it.id }
        ) { feedPost ->

            PostCard(
                feedPost = feedPost,
                onCommentClickListener = {
                    onCommentClickListener(feedPost)
                },
                onLikeClickListener = {
                    viewModel.changeLikeStatus(feedPost)
                },
                onFavouriteClickListener = {
                    viewModel.changeFavouriteStatus(feedPost)
                }
            )

        }

    }
}