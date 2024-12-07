package com.anikinkirill.tapyou

import android.app.Application
import com.anikinkirill.tapyou.core.network.networkModule
import com.anikinkirill.tapyou.data.di.dataModule
import com.anikinkirill.tapyou.domain.di.domainModule
import com.anikinkirill.tapyou.presentation.di.presentationModule
import org.koin.core.context.GlobalContext

class TapYouApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeKoin()
    }

    private fun initializeKoin() {
        GlobalContext.startKoin {
            modules(networkModule)
            modules(domainModule)
            modules(dataModule)
            modules(presentationModule)
        }
    }
}