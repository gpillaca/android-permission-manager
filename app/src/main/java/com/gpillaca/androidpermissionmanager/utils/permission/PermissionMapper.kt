package com.gpillaca.androidpermissionmanager.utils.permission

object PermissionMapper {

    fun mapToAndroidPermissionList(permissionWrapperList: List<PermissionWrapper>): List<String> {
        return permissionWrapperList.map { it.permission }
    }

    fun mapToPermissionWrapperList(permissions : List<String>) : List<PermissionWrapper> {
        val permissionWrapper = arrayListOf<PermissionWrapper>()
        permissions.map { androidPermission ->
            PermissionWrapper::class.sealedSubclasses.forEach {
                if (it.objectInstance?.permission == androidPermission) {
                    permissionWrapper.add(it.objectInstance!!)
                }
            }
        }
        return permissionWrapper
    }

    fun mapToPermissionWrapper(permission: String): PermissionWrapper {
        return PermissionWrapper::class.sealedSubclasses.first {
            it.objectInstance?.permission == permission
        }.objectInstance!!
    }

    fun getPermissionByType(type: PermissionType): List<PermissionWrapper> {
        val permissionWrapper = arrayListOf<PermissionWrapper>()
        PermissionWrapper::class.sealedSubclasses.forEach {
            if (it.objectInstance?.type == type) {
                permissionWrapper.add(it.objectInstance!!)
            }
        }
        return permissionWrapper
    }
}
