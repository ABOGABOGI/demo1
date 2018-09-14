package com.solarnet.demo.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.service.autofill.SaveInfo
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
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
    private var url: String? = null
    internal var savings: Savings? = null


    override fun  onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        accountImage.setOnClickListener{
            showdialog()
        }

        if(Savings.getProfilePicture() == null){
            url = "http://demo.sistemonline.biz.id/public/people/images/mypic.jpg"
        }else{
            url = Savings.getProfilePicture()
        }

        Glide.with(this)
                .load(url)
                .into(accountImage);


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

        account_names = findViewById(R.id.input_account_name)
        account_ktps = findViewById(R.id.input_account_ktp)
        account_numbers = findViewById(R.id.input_account_number)
        account_alamats = findViewById(R.id.input_account_alamat)
        account_kotas = findViewById(R.id.input_account_kota)
        account_provinsis = findViewById(R.id.input_account_provinsi)


        var content_name = findViewById(R.id.content_name) as TextView
        content_name.setText(Savings.getAccountName())

        var content_ktp = findViewById(R.id.content_ktp_number) as TextView
        content_ktp.setText(Savings.getAccountKtp())

        var content_account_number = findViewById(R.id.content_account_number) as TextView
        content_account_number.setText(Savings.getAccountNumber())

        var content_alamat = findViewById(R.id.content_alamat) as TextView
        content_alamat.setText(Savings.getAlamat())

        var content_kota = findViewById(R.id.content_kota) as TextView
        content_kota.setText(Savings.getKota())

        var content_provinsi = findViewById(R.id.content_provinsi) as TextView
        content_provinsi.setText(Savings.getProvinsi())




    }

    private fun savedataAccount() {
        account_name = account_names.toString()
        Savings.saveAccountName(account_name)

        account_ktp = account_ktps.toString()
        Savings.saveAccountKtp(account_ktp)

        account_number = account_numbers.toString()
        Savings.saveAccountNumber(account_number)

        account_alamat = account_alamats.toString()
        Savings.saveAlamat(account_alamat)

        account_kota = account_kotas.toString()
        Savings.saveKota(account_kota)

        account_provinsi = account_provinsis.toString()
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