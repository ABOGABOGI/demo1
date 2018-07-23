package com.solarnet.demo.data.util

import android.text.method.TextKeyListener
import java.text.NumberFormat
import java.util.*
import com.solarnet.demo.R.mipmap.ic_launcher
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v4.content.res.ResourcesCompat
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.solarnet.demo.data.product.Product
import java.nio.charset.Charset


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

        fun createProductJsonQR(product : Product) : String {
            return "{\"code\":\"${product.code}\",\"desc\":\"${product.description}\"}"
        }

        fun createQRCode(qrCodeData: String, charset: String, hintMap: Map<EncodeHintType, *>,
                         qrCodeheight: Int, qrCodewidth: Int): Bitmap? {

            var bitmap : Bitmap? = null
            try {
                //generating qr code.
                val matrix = MultiFormatWriter().encode(String(qrCodeData.toByteArray(charset(charset)),
                        charset(charset)),
                        BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap)
                //converting bitmatrix to bitmap

                val width = matrix.width
                val height = matrix.height
                val pixels = IntArray(width * height)
                // All are 0, or black, by default
                for (y in 0 until height) {
                    val offset = y * width
                    for (x in 0 until width) {
                        //for black and white
                        //pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
                        //for custom color
                        pixels[offset + x] = if (matrix.get(x, y))
                            Color.BLACK
                        else
                            Color.WHITE
                    }
                }
                //creating bitmap
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                bitmap?.setPixels(pixels, 0, width, 0, 0, width, height)

                //getting the logo
//                val overlay = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)
//                //setting bitmap to image view
//                imageViewBitmap.setImageBitmap(mergeBitmaps(overlay, bitmap))

            } catch (er: Exception) {
                Log.e("QrGenerate", er.message)
            }
            return bitmap
        }
    }
}