package com.hairulos.core.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.BatteryManager
import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/** Observes battery and default-network changes and emits a fresh [DeviceStatus] on each change. */
class DeviceStatusMonitor(context: Context) {
    private val appContext = context.applicationContext

    fun observe(): Flow<DeviceStatus> = callbackFlow {
        val cm = appContext.getSystemService(ConnectivityManager::class.java)
        val state = AtomicReference(DeviceStatus())

        fun update(transform: (DeviceStatus) -> DeviceStatus) {
            trySend(state.updateAndGet { transform(it) })
        }

        val batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) update { it.withBattery(intent) }
            }
        }
        val sticky = appContext.registerReceiver(
            batteryReceiver,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED),
        )
        if (sticky != null) update { it.withBattery(sticky) }

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                update { it.copy(network = cm.currentKind()) }
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                update { it.copy(network = cm.currentKind()) }
            }

            override fun onLost(network: Network) {
                update { it.copy(network = NetworkKind.NONE) }
            }
        }
        cm.registerDefaultNetworkCallback(networkCallback)
        update { it.copy(network = cm.currentKind()) }

        awaitClose {
            appContext.unregisterReceiver(batteryReceiver)
            cm.unregisterNetworkCallback(networkCallback)
        }
    }
}

private fun ConnectivityManager.currentKind(): NetworkKind {
    val network = activeNetwork ?: return NetworkKind.NONE
    val caps = getNetworkCapabilities(network) ?: return NetworkKind.NONE
    return when {
        caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkKind.WIFI
        caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkKind.CELLULAR
        caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkKind.ETHERNET
        else -> NetworkKind.OTHER
    }
}

private fun DeviceStatus.withBattery(intent: Intent): DeviceStatus {
    val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
    val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
    val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
    val percent = if (level < 0 || scale <= 0) -1 else level * 100 / scale
    val charging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
        status == BatteryManager.BATTERY_STATUS_FULL
    return copy(batteryPercent = percent, isCharging = charging)
}
