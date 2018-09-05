package com.solarnet.demo.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.solarnet.demo.R
import com.solarnet.demo.util.Savings
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_profil.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class AccountActivity : AppCompatActivity (){

    private var account_name: String? = null
    private var account_ktp: String? = null
    private var account_number: String? = null
    private var account_alamat: String? = null
    private var account_kota: String? = null
    private var account_provinsi: String? = null
    private var account_names: EditText? = null
    private var account_ktps: EditText? = null
    private var account_numbers: EditText? = null
    private var account_alamats: EditText? = null
    private var account_kotas: EditText? = null
    private var account_provinsis: EditText? = null

    private val GALLERY = 1
    private val CAMERA = 2


    override fun  onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        accountImage.setOnClickListener{
            showdialog()
        }

        var textview_profile = findViewById(R.id.account_profile) as TextView
        textview_profile.setText(Savings.getName())
        val inputAccount = findViewById(R.id.input_profile) as View
        val accountInfo = findViewById(R.id.content_profile) as View
        var btn_edit = findViewById(R.id.edit_account) as ImageButton
        var btn_save = findViewById(R.id.save_account) as Button
        btn_edit.setOnClickListener{
            inputAccount.visibility = View.VISIBLE
            accountInfo.visibility = View.GONE
        }
        btn_save.setOnClickListener {
            inputAccount.visibility = View.GONE
            accountInfo.visibility = View.VISIBLE
            savedataAccount()
        }

        account_names = findViewById(R.id.account_name)
        account_ktps = findViewById(R.id.account_ktp)
        account_numbers = findViewById(R.id.number_account)
        account_alamats = findViewById(R.id.account_alamat)
        account_kotas = findViewById(R.id.account_kota)
        account_provinsis = findViewById(R.id.account_provinsi)


        val profilename = findViewById(R.id.nameProfile) as TextView
//        profilename.setText(Savings.getName())
        if(Savings.getName() == null){
            profilename.setText("Demo")
        }
        val profilektp = findViewById(R.id.KtpProfile) as TextView
        profilektp.setText(Savings.getAccountKtp())
        val profilenumber = findViewById(R.id.number_Profile) as TextView
        profilenumber.setText(Savings.getAccountNumber())
        val profilealamat = findViewById(R.id.AlamatProfile) as TextView
        profilealamat.setText(Savings.getAlamat())
        val profilekota = findViewById(R.id.KotaProfile) as TextView
        profilekota.setText(Savings.getKota())
        val profileprovinsi = findViewById(R.id.provinsiProfile) as TextView
        profileprovinsi.setText(Savings.getProvinsi())


    }

    private fun savedataAccount() {
        account_name = account_names.toString()
        account_ktp = account_ktps.toString()
        account_number = account_numbers.toString()
        account_alamat = account_alamats.toString()
        account_kota = account_kotas.toString()
        account_provinsi = account_provinsis.toString()
        Savings.saveName(account_name)
        Savings.saveAccountKtp(account_ktp)
        Savings.saveAccountNumber(account_number)
        Savings.saveAlamat(account_alamat)
        Savings.saveKota(account_kota)
        Savings.saveProvinsi(account_provinsi)
    }



    private fun showdialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Photo")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ){dialog, which ->
            when(which){
                0 -> getfromgalery()
                1 -> takephoto()
            }
        }
        pictureDialog.show()
    }

    private fun getfromgalery() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takephoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GALLERY){
            if (data == null){
                val ContentURI = data !!.data
                try{
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, ContentURI)
                    val path = saveImage(bitmap)
                    Toast.makeText(this@AccountActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
                    ImageProfile!!.setImageBitmap(bitmap)
                }catch (e: IOException){
                    e.printStackTrace()
                    Toast.makeText(this@AccountActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if (requestCode == CAMERA){
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            ImageProfile!!.setImageBitmap(thumbnail)
            saveImage(thumbnail)
            Toast.makeText(this@AccountActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImage(thumbnail: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {
            wallpaperDirectory.mkdirs()
        }
        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                    arrayOf(f.getPath()),
                    arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }

    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
    }


}