package com.anikinkirill.tapyou.data.api

import com.anikinkirill.tapyou.data.model.TapYouPointsDto
import com.anikinkirill.tapyou.domain.Resource
import retrofit2.http.GET
import retrofit2.http.Query

private const val pointsUrl = "api/test/points"

interface TapYouPointsApi {

    @GET(pointsUrl)
    suspend fun getTapYouPoints(@Query("count") count: Int): Resource<TapYouPointsDto>
}