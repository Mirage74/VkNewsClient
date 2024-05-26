package com.balex.fbnewsclient.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class StatisticItem(
    val type: StatisticType,
    val count: Int = Random.nextInt(1, 1499)
): Parcelable

enum class StatisticType {
    VIEWS, COMMENTS, SHARES, LIKES,
}
