package com.anikinkirill.tapyou.data.di

import com.anikinkirill.tapyou.data.api.TapYouPointsApi
import com.anikinkirill.tapyou.data.repo.TapYouPointsRepositoryImpl
import com.anikinkirill.tapyou.domain.repo.TapYouPointsRepository
import com.anikinkirill.tapyou.data.mapper.TapYouPointsMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit

internal val dataModule = module {

    singleOf(::TapYouPointsMapper)

    single<TapYouPointsRepository> {
        TapYouPointsRepositoryImpl(
            mapper = get(),
            remoteApi = get(),
        )
    }

    single<TapYouPointsApi> {
        val retrofit = get<Retrofit>()
        retrofit.create(TapYouPointsApi::class.java)
    }
}