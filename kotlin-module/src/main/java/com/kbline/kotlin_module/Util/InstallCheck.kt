package com.kbline.kotlin_module.Util

import android.content.Context

object InstallCheck {

    fun PackageName(
        context: Context,
        pkgName: String
    ): Boolean {
        if (pkgName != null) {
            try {
                val pm = context.packageManager
                pm.getInstallerPackageName(pkgName)
                return true
            } catch (e: Exception) {
            }
        }
        return false
    }

}