package com.example.cloneinstagram.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.cloneinstagram.R
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {
    var PICK_IMAGE_FROM_ALBUM = 0
    var storage : FirebaseStorage? = null
    var photoUri : Uri? = null
    private lateinit var addphoto_btn_upload : Button
    private lateinit var addphoto_image : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        //initate storage
        storage = FirebaseStorage.getInstance()

        //Open the album
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type="Image/*"
        startActivityForResult(photoPickerIntent,PICK_IMAGE_FROM_ALBUM)

        //add image upload event
        addphoto_btn_upload.setOnClickListener{
            contentUpload()
        }
        addphoto_btn_upload=findViewById(R.id.addphoto_btn_upload)
    }


    override fun PreferenceManager.OnActivityResultListener(requestCode :Int, resultCode: Int, data: Intent?) {
       super.onActivityResult(requestCode, resultCode, data)
       if(requestCode == PICK_IMAGE_FROM_ALBUM){
           if(resultCode == Activity.RESULT_OK){
               //this is path to the selected image
               photoUri = data?.data
               addphoto_image.setImageURI(photoUri)
               addphoto_image=findViewById(R.id.addphoto_image)
           }else {
               //Exit the addPhotoActivity if you leave the album without selecting it
               finish()
               }
       }
    }
    fun contentUpload() {
        //Make filename

        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName ="IMAGE_" + timestamp + "_.png"

        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        //FileUpload
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            Toast.makeText(this,getString(R.string.upload_success),Toast.LENGTH_LONG).show()
        }
    }
}