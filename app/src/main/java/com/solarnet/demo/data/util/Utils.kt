package com.solarnet.demo.data.util

import android.text.method.TextKeyListener
import java.text.NumberFormat
import java.util.*

class Utils {
    companion object {
        fun currencyString(amount : Int) : String {
            return "Rp. " + NumberFormat.getNumberInstance(Locale.GERMAN).format(amount)
        }

        fun fromCurrencyString(str : String) : Int? {
            return str.replace("[^\\d]".toRegex(), "").toIntOrNull()
        }

        fun formatDate(date : Date, isCapitalize: Boolean = false) : String {
            var res = "Today"

            if (isCapitalize) {
                res = res.toUpperCase()
            }

            return res
        }

        fun filterNonDigit(input : String) : String {
            var output = ""
            input.forEach { c ->
                if (c.isDigit()) {
                    output += c
                }
            }
            return output
        }
    }
}