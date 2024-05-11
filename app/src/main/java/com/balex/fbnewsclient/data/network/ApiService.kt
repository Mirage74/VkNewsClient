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
        const val ACCESS_TOKEN = "EAANutEDbvyUBO2NmnVWlAUKgzEbZARN9WDsPjTn0u3apnC0QXXbC8wsMvoUkLl7mgw017RIjWIZCBripZBfAW6ZCr323xcr5D2jpLL3T49FcxHRVFv9Gx55m8q7UYv59yQOF3aHZCInjttTjVfprKvKuXhyK1hSNvwCT11PbhtLlCfouFIpaAis573EGLgAUoAkv0uCcchYgaJZASJG9lqb3J1NzMMG1wbW8MlFQnoSlZCRDxkLUv7kAgZDZD"
    }
}

