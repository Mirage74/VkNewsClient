package com.balex.fbnewsclient.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostDto(
    @SerializedName("created_time")
    val createdTime: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("message")
    val message: String
) : Parcelable
