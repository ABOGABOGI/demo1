package com.solarnet.demo.activity.payment

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.MenuItem
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.solarnet.demo.R
import com.solarnet.demo.data.product.ProductRepository
import com.solarnet.demo.data.util.Utils
import kotlinx.android.synthetic.main.activity_product_qr.*
import android.media.MediaScannerConnection.scanFile
import android.graphics.Bitmap
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import java.io.File
import java.io.FileOutputStream
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import java.io.FileNotFoundException
import java.io.IOException


class ProductQrActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_JSON = "json"
        const val EXTRA_NAME = "name"
        const val EXTRA_PRICE = "price"
        const val EXTRA_ID = "id"
    }

    private lateinit var mProductRepository : ProductRepository
    private var mProductId : Long = -1L
    private var mProductName : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_qr)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mProductRepository = ProductRepository(application)

        val json = intent.getStringExtra(EXTRA_JSON)
        generateQr(json)

        mProductName = intent.getStringExtra(EXTRA_NAME)
        val price = intent.getIntExtra(EXTRA_PRICE, 0)
        mProductId = intent.getLongExtra(EXTRA_ID, -1)
        Log.i("Test", "Product ID: $mProductId")
        textName.text = mProductName
        textQrPrice.text = Utils.currencyString(price)

        buttonCancel.setOnClickListener{
            showDeleteAlert()
        }
        buttonSave.setOnClickListener{
            if (saveQr()) {
                Toast.makeText(this, "Save QR successful!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun generateQr(json : String) {
        try {
            //setting size of qr code
            val width =300
            val height = 300
            var smallestDimension : Int = height
            if (width < height)  smallestDimension = width

            var qrCodeData = json

            //setting parameters for qr code
            val charset = "UTF-8"
            val hintMap = HashMap<EncodeHintType, ErrorCorrectionLevel>()
            hintMap[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            val bitmap = Utils.createQRCode(qrCodeData, charset, hintMap, smallestDimension, smallestDimension);
            if (bitmap != null) {
                imageQr.setImageBitmap(bitmap)
            }
        } catch (ex : Exception) {
            Log.e("QrGenerate",ex.message)
        }
    }

    fun showDeleteAlert() {
        var builder : AlertDialog.Builder? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            builder = AlertDialog.Builder(this)
        }
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes
                ) { _, _ ->
                    mProductRepository.delete(mProductId)
                    finish()
                }
                .setNegativeButton(android.R.string.no, DialogInterface.OnClickListener {
                    dialog, _ ->
                    dialog.dismiss()
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    fun saveQr() : Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    100)
            return false
        }

        imageQr.buildDrawingCache()

        val bmp = imageQr.drawingCache

        val storageLoc = Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        //context.getExternalFilesDir(null);

        val file = File(storageLoc, mProductName + ".jpg")

        try {
            val fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()

            scanFile(this, Uri.fromFile(file))

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

        return true
    }

    private fun scanFile(context: Context, imageUri: Uri) {
        val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        scanIntent.data = imageUri
        context.sendBroadcast(scanIntent)

    }
}
