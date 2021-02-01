package com.example.cloneinstagram.main

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cloneinstagram.R
import com.example.cloneinstagram.main.home.DetailViewFragment
import com.example.cloneinstagram.main.alarm.*
import com.example.cloneinstagram.main.grid.GridFragment
import com.example.cloneinstagram.main.upload.AddPhotoActivity
import com.example.cloneinstagram.main.user.UserFragment
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        setToolbarDefualt()
        if(item.itemId== R.id.action_add_photo){
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startActivity(Intent(this, AddPhotoActivity::class.java))
            }
            return true
        }
        if(item.itemId== R.id.action_account){
           var userFragment = UserFragment()
           var bundle = Bundle()
           var uid = FirebaseAuth.getInstance().currentUser?.uid
            bundle.putString("destinationUid",uid)
            userFragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.main_content, userFragment).commit()
            return true
        }
        val newFragment = when (item.itemId) {
            R.id.action_home -> DetailViewFragment()
            R.id.action_search -> GridFragment()
            R.id.action_favorite_alarm -> AlarmFragment()
            else->null
        }?:run {
            return false
        }
        supportFragmentManager.beginTransaction().replace(R.id.main_content, newFragment).commit()
        return true
    }
    fun setToolbarDefualt() {
        toolbar_username.visibility = View.GONE
        toolbar_btn_back.visibility = View.GONE
        toolbar_title_image.visibility =View.VISIBLE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(
            this,
            arrayOf((android.Manifest.permission.READ_EXTERNAL_STORAGE)),
            1
        )
        initActivity()
    }


    
    private fun initActivity() {
        initDTO()
        initBottomNavigation()
        initOnClickListener()
    }

    private fun initDTO() {

    }

    private fun initOnClickListener() {

    }

    private fun initBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        //Set default screen
        bottom_navigation.selectedItemId =
            R.id.action_home
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == UserFragment.PICK_PROFILE_FROM_ALBUM && resultCode == Activity.RESULT_OK){
            var imageUri = data?.data
            var uid = FirebaseAuth.getInstance().currentUser?.uid
            var storageRef = FirebaseStorage.getInstance().reference.child("userProfileImages").child(uid!!)
            storageRef.putFile(imageUri!!).continueWithTask { task:Task<UploadTask.TaskSnapshot> ->
                    return@continueWithTask storageRef.downloadUrl

            }.addOnSuccessListener { uri ->
                var map = HashMap<String, Any>()
                map["images"]= uri.toString()
                FirebaseFirestore.getInstance().collection("profileImages").document(uid).set(map)
            }
        }
    }

}