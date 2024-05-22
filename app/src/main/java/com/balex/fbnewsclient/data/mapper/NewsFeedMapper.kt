package com.balex.fbnewsclient.data.mapper

import android.util.Log
import com.balex.fbnewsclient.data.model.PostsDto
import com.balex.fbnewsclient.domain.entity.FeedPost

class NewsFeedMapper {

    val TAG = "NewsFeedMapper"

    fun mapResponseToPosts(responseDto: PostsDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()

        val paging = responseDto.paging
        Log.d(TAG, paging.toString())
        val posts = responseDto.data.stream().filter {
            e -> ((e.message != null) && (e.message.isNotEmpty()))
        }
        for (post in posts) {

            val feedPost = FeedPost(
                id = post.id,
                publicationDate = correctDate(post.createdTime),
                contentText = post.message,
            )
            result.add(feedPost)
        }
        return result
    }

private fun correctDate(date: String): String {
    var str = date.replace("T", ", ")
    str = str.substring(0, str.indexOf(",") + 7)
    return str
}

}