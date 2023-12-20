package com.gpillaca.androidpermissionmanager.utils.permission

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.gpillaca.androidpermissionmanager.R
object PermissionContainer {

    private var allPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private var allPermissions33 = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.POST_NOTIFICATIONS
    )

    private fun getPermissions(): Array<String> {
        val permissions: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            allPermissions33
        } else {
            allPermissions
        }
        return permissions
    }

    fun getPermissionsWrapper() = getPermissions().map {
        PermissionMapper.mapToPermissionWrapper(
            it
        )
    }

    private fun getMandatoryPermissionsType(): Array<PermissionType> {
        return getPermissionsWrapper().map { it.type }.toSet().toTypedArray()
    }

    fun getMandatoryPermissionsWrapper() : List<PermissionWrapper> {
        val permissionWrapper = arrayListOf<PermissionWrapper>()
        getMandatoryPermissionsType().forEach {
            permissionWrapper.addAll(PermissionMapper.getPermissionByType(it))
        }
        return permissionWrapper
    }

    fun checkNotificationsEnabled(context: Context) = when {
        NotificationManagerCompat.from(context).areNotificationsEnabled().not() -> false
        else -> {
            NotificationManagerCompat.from(context).notificationChannels.firstOrNull { channel ->
                channel.importance == NotificationManager.IMPORTANCE_NONE
            } == null
        }
    }

    fun cameraHasPermission(context: Context): Boolean =
        (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED)

    fun locationHasPermission(context: Context): Boolean =
        (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)

    private fun storageHasPermissionOld(context: Context): Boolean =
        (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private fun storageHasPermission33(context: Context): Boolean =
        (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED)

    fun storageHasPermission(context: Context): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storageHasPermission33(context)
        } else {
            storageHasPermissionOld(context)
        }


    fun askForPermissions(context: AppCompatActivity) : List<PermissionWrapper>{
        val permissions = ArrayList<String>()

        for (s in getPermissions()) {
            if (ContextCompat.checkSelfPermission(context, s) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(s)
            }
        }

        return permissions.map { PermissionMapper.mapToPermissionWrapper(it) }
    }

    fun getIntentToSettings(context: Context): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
    }

    fun showRationaleDialog(
        context: Activity,
        title: String,
        message: String,
        buttonTitle: String,
        negativeCallback: (() -> Unit)? = null,
        positiveCallback: () -> Unit
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(buttonTitle) { _, _ ->
                positiveCallback.invoke()
            }
            .setNegativeButton(context.getString(R.string.CANCEL)) { dialog, _ ->
                negativeCallback?.invoke()
                dialog.dismiss()
            }
        builder.create().show()
    }

    fun showRequestPermissionsDialog(
        context: Context,
        title: String,
        message: String,
        buttonTitle: String,
        negativeCallback: (() -> Unit)?,
        positiveCallback: () -> Unit
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(buttonTitle) { _, _ ->
                positiveCallback.invoke()
            }
            .setNegativeButton(context.getString(R.string.CANCEL)) { dialog, _ ->
                negativeCallback?.invoke()
                dialog.dismiss()
            }
        builder.create().show()
    }

}
