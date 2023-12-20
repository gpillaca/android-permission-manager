package com.gpillaca.androidpermissionmanager.utils.permission

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference

class ActivityPermissionManager private constructor(private val activity: WeakReference<AppCompatActivity>) : PermissionManager() {

    private val permissionCheck = activity.get()?.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantResults ->
            sendResultAndCleanUp(grantResults)
        }

    companion object {
        fun from(activity: AppCompatActivity) = ActivityPermissionManager(WeakReference(activity))
    }

    override fun handlePermissionRequest() {
        activity.get()?.let { activity ->
            when {
                areAllPermissionsGranted(activity) -> sendPositiveResult()
                shouldShowPermissionRationale(activity) -> displayRationale(activity)
                else -> requestPermissions()
            }
        }
    }

    override fun requestPermissions() {
        permissionCheck?.launch(getPermissionList())
    }

}
