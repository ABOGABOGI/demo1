package com.solarnet.demo.activity.payment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.solarnet.demo.R

import kotlinx.android.synthetic.main.activity_qr.*
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject
import com.google.zxing.integration.android.IntentResult
import android.content.Intent




class QrActivity : BaseActivity() {
    //qr code scanner object
    private var integrator : IntentIntegrator? = null

    override fun next() {

    }

    fun startCamera() {

        // inisialisasi IntentIntegrator(scanQR)
        integrator = IntentIntegrator(this)
        integrator?.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
        integrator?.setPrompt("Scan QR Code")
        integrator?.setCameraId(0)  // Use a specific camera of the device
        integrator?.setOrientationLocked(true)
        integrator?.setBeepEnabled(true)
        integrator?.captureActivity = CaptureActivity::class.java
        integrator?.initiateScan()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)
        setSupportActionBar(toolbar)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    // converting the data json
                    val json = JSONObject(result.contents)
                    // atur nilai ke textviews
                    val code = json.getString("code")
                    val desc = json.getString("desc")
                    Toast.makeText(this, "Code : $code\nDesc: $desc", Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // jika format encoded tidak sesuai maka hasil
                    // ditampilkan ke toast
                    Toast.makeText(this, result.contents, Toast.LENGTH_SHORT).show()
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}


