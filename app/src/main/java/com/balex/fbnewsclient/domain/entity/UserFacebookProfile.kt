package com.balex.fbnewsclient.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserFacebookProfile(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("id")
    val id: String = ""
) : Parcelable
