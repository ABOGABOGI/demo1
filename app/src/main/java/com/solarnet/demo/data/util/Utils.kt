package com.solarnet.demo.data.util

import android.text.method.TextKeyListener
import java.util.*

class Utils {
    companion object {
        fun currencyString(amount : Int) : String {
            return "Rp. $amount"
        }

        fun formatDate(date : Date, isCapitalize: Boolean = false) : String {
            var res = "Today"

            if (isCapitalize) {
                res = res.toUpperCase()
            }

            return res
        }
    }
}