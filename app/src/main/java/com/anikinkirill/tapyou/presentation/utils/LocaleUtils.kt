package com.anikinkirill.tapyou.presentation.utils

import android.content.Context
import java.util.Locale

fun Context?.getStringSupportedLocale(resId: Int, vararg formatArgs: Any?): String {
    this?.let {
        return try {
            String.format(
                getDefaultSupportedLocale(),
                getString(resId),
                *formatArgs
            )
        } catch (e: Exception) {
            getString(resId)
        }
    }
    return ""
}

fun getDefaultSupportedLocale(): Locale {
    val locale = Locale.getDefault()
    supportedLanguages.find { it == locale.language }?.let {
        return locale
    }
    return Locale.ENGLISH
}

val supportedLanguages = listOf("en", "ru")