package com.solarnet.demo.activity.login


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.solarnet.demo.MainActivity
import com.solarnet.demo.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginAcitivty : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener{
    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d("bett", "onConnectionFailed:" + connectionResult);
    }

    private val RC_SIGN_IN = 9001
    private var mGoogleApiClient: GoogleApiClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

      var btn_logins = findViewById(R.id.btn_submit) as Button

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        btn_logins?.setOnClickListener(View.OnClickListener {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent, RC_SIGN_IN)
//            startActivity(Intent(this@LoginAcitivty, MainActivity::class.java))
        })
    }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
//            handleSignInResult(result)
            startActivity(Intent(this@LoginAcitivty, MainActivity::class.java))
        }
    }

        private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            startActivity(Intent(this@LoginAcitivty, MainActivity::class.java))
        }
    }
}
