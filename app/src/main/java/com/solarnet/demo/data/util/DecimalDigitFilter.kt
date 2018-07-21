package com.solarnet.demo.data.util

import android.text.InputFilter
import android.text.Spanned

class DecimalDigitFilter : InputFilter {
    override fun filter(source: CharSequence, start: Int, end: Int,
                        dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
        for (i in start until end) {
            if (!Character.isDigit(source[i])) {
                return ""
            }
        }
        return null
    }
}