package com.balex.fbnewsclient.data.network

import com.balex.fbnewsclient.data.model.PostsDto
import com.balex.fbnewsclient.domain.UserFacebookProfile
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("me")
    suspend fun getUserProfile(
        @Query("access_token") token: String = ACCESS_TOKEN
    ): UserFacebookProfile

    @GET("{id}/posts")
    suspend fun getUserPosts(
        @Path("id") id: String,
        @Query("access_token") token: String = ACCESS_TOKEN
    ): PostsDto

    companion object {
        const val ACCESS_TOKEN = "EAANutEDbvyUBO5s6bqoIisOlch0zdNasLV7xHZCRMC6PzkpSFzh6VCOQVZCrUKjQ8yzDw00yCarEbjFzHT3MXYtap9ASraoBWgc0r5gt30YTTCK97spe1bXMSxNM1oHOog6ZCALMkKlPeW9K865v0yY8xDfTUZAdQeirvIMZBTIupUvqXz8KNmJsl"
    }
}

