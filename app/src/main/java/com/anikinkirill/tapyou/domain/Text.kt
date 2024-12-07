package com.anikinkirill.tapyou.domain

import android.content.Context
import android.widget.TextView
import androidx.annotation.StringRes
import com.anikinkirill.tapyou.presentation.utils.getStringSupportedLocale
import com.anikinkirill.tapyou.presentation.utils.setTextIfChanged

sealed class Text {
    data class Resource(@StringRes val resId: Int) : Text()

    data class Simple(val text: String) : Text()

    companion object {
        fun res(@StringRes resId: Int) = Resource(resId)
        fun simple(text: String) = Simple(text)
    }
}

fun Text.getString(context: Context): String {
    return when (this) {
        is Text.Resource -> {
            context.getString(resId)
        }

        is Text.Simple -> {
            text
        }
    }
}

fun TextView.setText(clause: Text?) {
    when (clause) {
        is Text.Resource -> setTextIfChanged(context.getStringSupportedLocale(clause.resId))
        is Text.Simple -> setTextIfChanged(clause.text)
        null -> setTextIfChanged("")
    }
}