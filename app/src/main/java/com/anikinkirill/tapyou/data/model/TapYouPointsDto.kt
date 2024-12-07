package com.anikinkirill.tapyou.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TapYouPointsDto(
    @SerialName("points")
    val points: List<TapYouPointDto>?
)

@Serializable
data class TapYouPointDto(
    @SerialName("x")
    val x: Double?,
    @SerialName("y")
    val y: Double?,
)