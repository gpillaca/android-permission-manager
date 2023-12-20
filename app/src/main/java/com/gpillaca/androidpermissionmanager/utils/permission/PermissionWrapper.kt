package com.gpillaca.androidpermissionmanager.utils.permission

import android.Manifest

sealed class PermissionWrapper(val permission: String, val type: PermissionType) {
    object LocationPermission : PermissionWrapper(Manifest.permission.ACCESS_FINE_LOCATION,
        PermissionType.LOCATION_PERMISSION
    )
    object CameraPermission : PermissionWrapper(Manifest.permission.CAMERA,
        PermissionType.CAMERA_PERMISSION
    )
    object ExternalStoragePermission : PermissionWrapper(Manifest.permission.WRITE_EXTERNAL_STORAGE,
        PermissionType.STORAGE_PERMISSION
    )

    object ReadExternalPermission : PermissionWrapper(Manifest.permission.READ_EXTERNAL_STORAGE,
        PermissionType.STORAGE_PERMISSION
    )
    object ReadMediaImages : PermissionWrapper(Manifest.permission.READ_MEDIA_IMAGES,
        PermissionType.STORAGE_PERMISSION
    )

    object PostNotificationPermission: PermissionWrapper(Manifest.permission.POST_NOTIFICATIONS,
        PermissionType.NOTIFICATION_PERMISSION
    )
}
