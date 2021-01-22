package com.example.cloneinstagram.navigation

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cloneinstagram.R
import com.example.cloneinstagram.navigation.model.ContentDTO
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class AddPhotoActivity : AppCompatActivity() {
    var PICK_IMAGE_FROM_ALBUM = 0 //request code
    var storage: FirebaseStorage? = null
    var photoUri: Uri? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        //initiate storage
        storage = FirebaseStorage.getInstance()


        //Open the album

        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "Image/*"
        startActivityForResult(photoPickerIntent,PICK_IMAGE_FROM_ALBUM)

        //add image upload event
        addphoto_btn_upload.setOnClickListener {
            contentUpload()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                //사진 선택 시, 이미지 경로가 넘어옴
                photoUri = data?.data
                addphoto_image.setImageURI(photoUri)

            } else {
                //취소버튼 눌러서 exit addPhotoAcitivity
                finish()
            }
        }
    }

    fun contentUpload() {
        //Make filename

        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"
        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        //promise method (구글 권장 방식)
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener{
            Toast.makeText(this, getString(R.string.upload_success), Toast.LENGTH_LONG).show()
        }


    }
}
