package com.anikinkirill.tapyou.domain.usecase

import com.anikinkirill.tapyou.domain.Resource
import com.anikinkirill.tapyou.domain.model.TapYouPoints
import com.anikinkirill.tapyou.domain.repo.TapYouPointsRepository

class GetTapYouPointsUseCase(
    private val repo: TapYouPointsRepository,
) {

    suspend fun execute(amount: Int): Resource<List<TapYouPoints>> {
        return repo.getPoints(amount)
    }
}