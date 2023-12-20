package com.gpillaca.androidpermissionmanager.utils.permission

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

class FragmentPermissionManager private constructor(private val fragment: WeakReference<Fragment>) : PermissionManager() {

    private val permissionCheck =
        fragment.get()?.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantResults ->
            sendResultAndCleanUp(grantResults)
        }

    companion object {
        fun from(fragment: Fragment) = FragmentPermissionManager(WeakReference(fragment))
    }

    override fun handlePermissionRequest() {
        fragment.get()?.let { fragment ->
            when {
                areAllPermissionsGranted(fragment) -> sendPositiveResult()
                shouldShowPermissionRationale(fragment) -> displayRationale(fragment.requireActivity() as AppCompatActivity)
                else -> requestPermissions()
            }
        }
    }

    override fun requestPermissions() {
        permissionCheck?.launch(getPermissionList())
    }
}
