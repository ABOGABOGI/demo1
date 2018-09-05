package com.solarnet.demo.activity

import android.app.AlertDialog
import android.content.Context
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
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.icon_list_item.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import android.content.SharedPreferences
import com.solarnet.demo.MainActivity


class ProfileActivity : AppCompatActivity() {

    private val GALLERY = 1
    private val CAMERA = 2
    internal var savings: Savings? = null

    private val SHARED_PREF_NAME = "mysharedpref"
    private val KEY_NAME = "keyname"


    override fun  onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        ImageProfile.setOnClickListener{
            showdialog()
        }


        val listviewProfile = findViewById(R.id.listview_profile) as ListView
//        val v = findViewById(R.id.input_profile) as View
//        val v1 = findViewById(R.id.content_profile) as View
        var textview_profile = findViewById(R.id.textview_profile) as TextView
        textview_profile.setText(Savings.getName())
//        var btn_save = findViewById(R.id.saveProfile) as Button
//        var btn_edit = findViewById(R.id.edit_profile) as ImageButton
//        btn_edit.setOnClickListener{
//            v.visibility = View.VISIBLE
//            v1.visibility = View.GONE
//            linearProfile.visibility = View.GONE
//        }
//
//        btn_save.setOnClickListener {
//            v.visibility = View.GONE
//            linearProfile.visibility = View.VISIBLE
//
//        }

        val values = arrayOf(
                "Account",
                "About",
                "Settings",
                "LogOut")

        val adapter = ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,values)
        listviewProfile.adapter = adapter

        listviewProfile.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            when(position){
                0 -> startActivity(Intent(this,AccountActivity::class.java))
                1 -> startActivity(Intent(this,MainActivity::class.java))
                2 -> startActivity(Intent(this,MainActivity::class.java))
                3 -> startActivity(Intent(this,MainActivity::class.java))
            }

        }


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
                    Toast.makeText(this@ProfileActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
                    ImageProfile!!.setImageBitmap(bitmap)
                }catch (e: IOException){
                    e.printStackTrace()
                    Toast.makeText(this@ProfileActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if (requestCode == CAMERA){
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            ImageProfile!!.setImageBitmap(thumbnail)
            saveImage(thumbnail)
            Toast.makeText(this@ProfileActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
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

