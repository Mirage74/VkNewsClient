package com.balex.fbnewsclient.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Collections

@Parcelize
data class PostsDto(
    @SerializedName("data")
    val data: List<PostDto> = Collections.emptyList(),
    @SerializedName("paging")
    val paging: PagingDto = PagingDto()
) : Parcelable
