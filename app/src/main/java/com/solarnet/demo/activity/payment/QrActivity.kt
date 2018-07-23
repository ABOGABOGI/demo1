package com.solarnet.demo.activity.payment

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
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
import android.graphics.Color
import android.view.Menu
import com.solarnet.demo.MyApp
import com.solarnet.demo.data.util.Utils
import kotlinx.android.synthetic.main.content_qr.*

class QrActivity : BaseActivity() {
    class AppViewModel : ViewModel() {
        var code : String = ""
        var desc : String = ""
        var price : Int = -1
        var initBalance = MyApp.instance.getBalance()
        var balance : Int = 0
        init {
            balance = initBalance
        }
    }

    //qr code scanner object
    private var integrator : IntentIntegrator? = null
    lateinit var mViewModel : AppViewModel

    override fun next() {

    }

    private fun getPrice(code : String, desc : String) {
        mViewModel.code = code
        mViewModel.desc = desc

        //hardcode for now
        mViewModel.price = 25000

        mViewModel.balance = mViewModel.initBalance - mViewModel.price

        textDescription.text = mViewModel.desc
        textPrice.text = Utils.currencyString(mViewModel.price)
        textBalance.text = Utils.currencyString(mViewModel.balance)

        if (mViewModel.balance < 0) {
            textBalance.setTextColor(Color.RED)
        } else {
            textBalance.setTextColor(Color.BLACK)
        }
    }

    private fun updateMenuNext() {
        menuNext?.isEnabled = mViewModel.balance >= 0 && mViewModel.price > 0
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.send_payment, menu)
        menuNext = menu.findItem(R.id.action_next)
        updateMenuNext()

        return true
    }

    override fun onBackPressed() {
        if (integrator == null) {
            back()
        } else {
            super.onBackPressed()
        }
    }
    override fun back() {
        startCamera()
    }

    private fun startCamera() {
        // inisialisasi IntentIntegrator(scanQR)
        integrator = IntentIntegrator(this)
        integrator?.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
        integrator?.setPrompt("Scan QR Code")
        integrator?.setCameraId(0)  // Use a specific camera of the device
        integrator?.setOrientationLocked(true)
        integrator?.setBeepEnabled(true)
//        integrator?.captureActivity = CaptureActivity::class.java
        integrator?.initiateScan()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)
        setSupportActionBar(toolbar)
        mViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        startCamera()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null && resultCode == RESULT_OK) {
            if (result.contents == null) {
                Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    // converting the data json
                    val json = JSONObject(result.contents)
                    // atur nilai ke textviews
                    val code = json.getString("code")
                    val desc = json.getString("desc")
                    //Toast.makeText(this, "Code : $code\nDesc: $desc", Toast.LENGTH_SHORT).show()
                    getPrice(code, desc)
                    integrator = null
                } catch (e: Exception) {
                    e.printStackTrace()
                    // jika format encoded tidak sesuai maka hasil
                    // ditampilkan ke toast
                    Toast.makeText(this, result.contents, Toast.LENGTH_SHORT).show()
                }

            }
        } else {
            //user cancel the camera
            finish()
        }
    }
}


