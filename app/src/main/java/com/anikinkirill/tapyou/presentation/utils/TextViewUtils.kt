package com.anikinkirill.tapyou.presentation.utils

import android.widget.TextView

/** @return true if new value has set */
fun TextView.setTextIfChanged(text: String): Boolean {
    if (this.text != text) {
        this.text = text
        return true
    }
    return false
}