package com.balex.fbnewsclient.domain

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import com.balex.fbnewsclient.R
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedPost(
    val id: String,
    val communityName: String = COMMUNITY_NAME,
    val publicationDate: String,
    val communityImageResId: Int = R.drawable.group_image,
    val contentText: String,
    val contentImageResId: Int = R.drawable.cat,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(StatisticType.VIEWS, 55),
        StatisticItem(StatisticType.COMMENTS, 12),
        StatisticItem(StatisticType.SHARES, 27),
        StatisticItem(StatisticType.LIKES, 395)
    )
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