package com.anikinkirill.tapyou.data.repo

import com.anikinkirill.tapyou.data.api.TapYouPointsApi
import com.anikinkirill.tapyou.data.mapper.TapYouPointsMapper
import com.anikinkirill.tapyou.domain.Resource
import com.anikinkirill.tapyou.domain.model.TapYouPoints
import com.anikinkirill.tapyou.domain.repo.TapYouPointsRepository

class TapYouPointsRepositoryImpl(
    private val remoteApi: TapYouPointsApi,
    private val mapper: TapYouPointsMapper,
) : TapYouPointsRepository {

    override suspend fun getPoints(amount: Int): Resource<List<TapYouPoints>> {
        val remotePoints = remoteApi.getTapYouPoints(amount)
        val domainPoints = mapper.map(remotePoints)
        return domainPoints
    }
}