package com.hairulos.core.device

import android.os.Build

enum class NetworkKind { NONE, WIFI, CELLULAR, ETHERNET, OTHER }

data class DeviceStatus(
    /** 0..100, or -1 when unknown. */
    val batteryPercent: Int = -1,
    val isCharging: Boolean = false,
    val network: NetworkKind = NetworkKind.NONE,
    val manufacturer: String = Build.MANUFACTURER,
    val model: String = Build.MODEL,
    val androidRelease: String = Build.VERSION.RELEASE,
    val apiLevel: Int = Build.VERSION.SDK_INT,
) {
    val isOnline: Boolean get() = network != NetworkKind.NONE
}
