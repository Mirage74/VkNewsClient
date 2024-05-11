package com.balex.fbnewsclient.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PagingDto(
    @SerializedName("previous")
    val previous: String = "",
    @SerializedName("next")
    val next: String = ""
) : Parcelable
