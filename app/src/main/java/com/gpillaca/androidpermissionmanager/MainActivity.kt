package com.gpillaca.androidpermissionmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gpillaca.androidpermissionmanager.utils.permission.ActivityPermissionManager
import com.gpillaca.androidpermissionmanager.utils.permission.PermissionWrapper

class MainActivity : AppCompatActivity() {

    private val permissionManager = ActivityPermissionManager.from(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
    }

    private fun checkPermission() {
        permissionManager
            .request(PermissionWrapper.LocationPermission)
            .rationaleTitle(getString(R.string.PERMISSION_REQUIRED_TITLE))
            .rationaleMessage(getString(R.string.PERMISSION_REQUIRED_MESSAGE))
            .checkPermission { isGranted ->
                if (isGranted) {
                    // Do something
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    // Do something
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
