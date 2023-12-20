package com.gpillaca.androidpermissionmanager.utils.permission

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gpillaca.androidpermissionmanager.R
abstract class PermissionManager {

    private val requiredPermissions = mutableListOf<PermissionWrapper>()
    private var rationaleTitle: String? = null
    private var rationaleMessage: String? = null

    protected var callback: (Boolean) -> Unit = {}
    protected var detailedCallback: (Map<PermissionWrapper,Boolean>) -> Unit = {}

    fun rationaleTitle(rationaleTitle: String): PermissionManager {
        this.rationaleTitle = rationaleTitle
        return this
    }

    fun rationaleMessage(rationaleMessage: String): PermissionManager {
        this.rationaleMessage = rationaleMessage
        return this
    }

    fun request(vararg permission: PermissionWrapper): PermissionManager {
        requiredPermissions.addAll(permission)
        return this
    }


    fun request(permission: List<PermissionWrapper>): PermissionManager {
        requiredPermissions.addAll(permission)
        return this
    }

    fun checkPermission(callback: (Boolean) -> Unit) {
        this.callback = callback
        handlePermissionRequest()
    }

    fun checkDetailedPermission(callback: (Map<PermissionWrapper,Boolean>) -> Unit) {
        this.detailedCallback = callback
        handlePermissionRequest()
    }

    abstract fun handlePermissionRequest()
    abstract fun requestPermissions()

    fun displayRationale(activity: AppCompatActivity) {
        PermissionContainer.showRationaleDialog(
            context = activity,
            title = rationaleTitle.toString(),
            message = rationaleMessage.toString(),
            buttonTitle = activity.getString(R.string.PERMISSION_REQUIRED_GO_TO_SETTINGS),
            negativeCallback = {},
            positiveCallback = {
                requestPermissions()
            })
    }

    private fun cleanUp() {
        requiredPermissions.clear()
        rationaleMessage = null
        rationaleTitle = null
        callback = {}
        detailedCallback = {}
    }

    protected fun sendResultAndCleanUp(grantResults: Map<String, Boolean>) {
        callback(grantResults.all { it.value })
        detailedCallback(grantResults.mapKeys { PermissionMapper.mapToPermissionWrapper(it.key) })
        cleanUp()
    }

    protected fun areAllPermissionsGranted(fragment: Fragment) =
        requiredPermissions.all { it.isGranted(fragment) }

    protected fun areAllPermissionsGranted(activity: AppCompatActivity) =
        requiredPermissions.all { it.isGranted(activity) }

    protected fun shouldShowPermissionRationale(fragment: Fragment) =
        requiredPermissions.any { it.requiresRationale(fragment) }

    protected fun shouldShowPermissionRationale(activity: AppCompatActivity) =
        requiredPermissions.any { it.requiresRationale(activity) }

    protected fun getPermissionList() : Array<String> {
        val permissions = arrayListOf<String>()
        requiredPermissions.forEach {
            permissions.add(it.permission)
        }
        return permissions.toTypedArray()
    }

    protected fun sendPositiveResult() {
        sendResultAndCleanUp(getPermissionList().associateWith { true })
    }
    private fun PermissionWrapper.isGranted(fragment: Fragment) = hasPermission(fragment, permission)

    private fun PermissionWrapper.isGranted(activity: AppCompatActivity) = hasPermission(activity, permission)
    private fun PermissionWrapper.requiresRationale(fragment: Fragment) = fragment.shouldShowRequestPermissionRationale(permission)

    private fun PermissionWrapper.requiresRationale(activity: AppCompatActivity) = activity.shouldShowRequestPermissionRationale(permission)


    protected fun hasPermission(fragment: Fragment, permission: String) =
        ContextCompat.checkSelfPermission(
            fragment.requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

    protected fun hasPermission(activity: AppCompatActivity, permission: String) =
        ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED

}
