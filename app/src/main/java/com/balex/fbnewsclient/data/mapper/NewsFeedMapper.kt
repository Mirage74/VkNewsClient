package com.balex.fbnewsclient.data.mapper

import com.balex.fbnewsclient.data.model.PostsDto
import com.balex.fbnewsclient.domain.FeedPost

class NewsFeedMapper {

    fun mapResponseToPosts(responseDto: PostsDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()

        val posts = responseDto.data
        for (post in posts) {
            val feedPost = FeedPost(
                id = post.id,
                publicationDate = post.createdTime,
                contentText = post.message,
            )
            result.add(feedPost)
        }
        return result
    }
}