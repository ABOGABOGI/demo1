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
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.icon_list_item.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private val GALLERY = 1
    private val CAMERA = 2



    override fun  onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        ImageProfile.setOnClickListener{
            showdialog()
        }
        var editview_profile = findViewById(R.id.editview_profile) as EditText
        var textview_profile = findViewById(R.id.textview_profile) as TextView
        var scrool_profile = findViewById(R.id.scroolview_edit_profile) as ScrollView
        var save_profile = findViewById(R.id.save_button) as Button
        var btn_edit = findViewById(R.id.edit_profile) as ImageButton
        btn_edit.setOnClickListener{
           editview_profile.visibility = View.VISIBLE
           textview_profile.visibility = View.GONE
            scrool_profile.visibility = View.VISIBLE
            save_profile.visibility = View.VISIBLE
        }

        save_profile.setOnClickListener {
            editview_profile.visibility = View.GONE
            textview_profile.visibility = View.VISIBLE
            scrool_profile.visibility = View.GONE
            save_profile.visibility = View.GONE
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

