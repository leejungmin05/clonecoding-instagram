package com.example.cloneinstagram.main

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cloneinstagram.R
import com.example.cloneinstagram.main.home.DetailViewFragment
import com.example.cloneinstagram.main.alarm.*
import com.example.cloneinstagram.main.grid.GridFragment
import com.example.cloneinstagram.main.upload.AddPhotoActivity
import com.example.cloneinstagram.main.user.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
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

        val newFragment = when (item.itemId) {
            R.id.action_home -> DetailViewFragment()
            R.id.action_search -> GridFragment()
            R.id.action_favorite_alarm -> AlarmFragment()
            R.id.action_account -> UserFragment()
            else->null
        }?:run {
            return false
        }
        supportFragmentManager.beginTransaction().replace(R.id.main_content, newFragment).commit()
        return true
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


}