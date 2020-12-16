package com.example.zenmood

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.zenmood.classes.SharedPreferences
import com.example.zenmood.classes.firebase
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var lytDrawer : DrawerLayout
    lateinit var drawer : NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this,R.id.nav_host)
        lytDrawer = findViewById(R.id.lyt_drawer)
        drawer = findViewById(R.id.drawer)

        NavigationUI.setupWithNavController(drawer,navController)
        NavigationUI.setupActionBarWithNavController(this,navController,lytDrawer)
        if (!checkPermissions()) {
            requestPermissions()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val sp : SharedPreferences = SharedPreferences(applicationContext)
        if(!sp.checkStayLogged()){
            firebase.logOut()
        }
    }

    override fun onStop() {
        super.onStop()
        val sp : SharedPreferences = SharedPreferences(applicationContext)
        if(!sp.checkStayLogged()){
            firebase.logOut()
        }
    }

    override fun onBackPressed() {
        if (lytDrawer.isDrawerOpen(GravityCompat.START))
            lytDrawer.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,lytDrawer)
    }

    private fun checkPermissions(): Boolean {
        var permissionState = 0

        val permissions = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        for (perm in permissions) {
            permissionState = ActivityCompat.checkSelfPermission(this, perm)
        }
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        for (perm in permissions) {
            ActivityCompat.shouldShowRequestPermissionRationale(this, perm)
        }
        startPermissionRequest(permissions)
    }

    private fun startPermissionRequest(perm: Array<String>) {
        ActivityCompat.requestPermissions(this, perm, 0)
    }
}