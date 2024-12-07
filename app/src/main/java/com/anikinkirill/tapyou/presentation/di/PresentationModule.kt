package com.anikinkirill.tapyou.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import com.anikinkirill.tapyou.presentation.chart.TapYouPointsChartViewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::TapYouPointsChartViewModel)
}