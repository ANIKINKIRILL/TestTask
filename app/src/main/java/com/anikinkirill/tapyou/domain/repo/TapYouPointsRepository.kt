package com.anikinkirill.tapyou.domain.repo

import com.anikinkirill.tapyou.domain.Resource
import com.anikinkirill.tapyou.domain.model.TapYouPoints

interface TapYouPointsRepository {

    suspend fun getPoints(amount: Int): Resource<List<TapYouPoints>>
}