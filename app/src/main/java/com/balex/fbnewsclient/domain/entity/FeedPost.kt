package com.balex.fbnewsclient.domain.entity

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Immutable
import androidx.navigation.NavType
import com.balex.fbnewsclient.R
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import javax.inject.Inject
import kotlin.random.Random

@Immutable
@Parcelize
data class FeedPost @Inject constructor(
    val id: String,
    val communityName: String = COMMUNITY_NAME,
    val publicationDate: String,
    val communityImageResId: Int = R.drawable.group_image,
    val contentText: String,
    val contentImageResId: Int = R.drawable.cat,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(StatisticType.VIEWS),
        StatisticItem(StatisticType.COMMENTS),
        StatisticItem(StatisticType.SHARES),
        StatisticItem(StatisticType.LIKES)
    ),
    val isLiked: Boolean = Random.nextBoolean(),
    val isFavourite: Boolean = false
) : Parcelable {

    companion object {
        const val COMMUNITY_NAME = "Bowling"

        val NavigationType: NavType<FeedPost> = object : NavType<FeedPost>(false) {

            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun get(bundle: Bundle, key: String): FeedPost? {
                return bundle.getParcelable(key, FeedPost::class.java)
            }

            override fun parseValue(value: String): FeedPost {
                return Gson().fromJson(value, FeedPost::class.java)
            }

            override fun put(bundle: Bundle, key: String, value: FeedPost) {
                bundle.putParcelable(key, value)
            }
        }
    }
}