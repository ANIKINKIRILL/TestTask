package com.anikinkirill.tapyou.data.mapper

import com.anikinkirill.tapyou.R
import com.anikinkirill.tapyou.data.model.TapYouPointsDto
import com.anikinkirill.tapyou.domain.Resource
import com.anikinkirill.tapyou.domain.Resource.Error
import com.anikinkirill.tapyou.domain.Text
import com.anikinkirill.tapyou.domain.model.TapYouPoints

class TapYouPointsMapper {

    fun map(pointsDtoResource: Resource<TapYouPointsDto>): Resource<List<TapYouPoints>> {
        return when (pointsDtoResource) {
            is Error -> Error(
                data = null,
                message = pointsDtoResource.message ?: Text.res(R.string.network_error),
            )
            is Resource.Success -> Resource.Success(
                mapPointsDtoToDomain(pointsDto = pointsDtoResource.data)
            )
            is Resource.SuccessWithNoResponseBody -> Resource.SuccessWithNoResponseBody()
        }
    }

    private fun mapPointsDtoToDomain(pointsDto: TapYouPointsDto?): List<TapYouPoints> {
        return pointsDto?.points.orEmpty().mapNotNull { dto ->
            val x = dto.x ?: return@mapNotNull null
            val y = dto.y ?: return@mapNotNull null
            TapYouPoints(x = x, y = y)
        }
    }
}