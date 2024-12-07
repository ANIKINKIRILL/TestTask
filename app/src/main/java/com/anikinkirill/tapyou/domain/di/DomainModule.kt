package com.anikinkirill.tapyou.domain.di

import com.anikinkirill.tapyou.domain.usecase.GetTapYouPointsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val domainModule = module {

    singleOf(::GetTapYouPointsUseCase)
}