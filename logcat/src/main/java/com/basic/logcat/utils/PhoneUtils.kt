package com.basic.logcat.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Locale


/**
 * 获取ip地址
 */

fun getIPAddress(useIPv4: Boolean = true): String? {
    try {
        val nis = NetworkInterface.getNetworkInterfaces()
        while (nis.hasMoreElements()) {
            val ni = nis.nextElement()
            // 防止小米手机返回10.0.2.15
            if (!ni.isUp) {
                continue
            }
            val addresses = ni.inetAddresses
            while (addresses.hasMoreElements()) {
                val inetAddress = addresses.nextElement()
                if (!inetAddress.isLoopbackAddress) {
                    val hostAddress = inetAddress.hostAddress
                    val isIPv4 = hostAddress!!.indexOf(':') < 0
                    if (useIPv4) {
                        if (isIPv4) {
                            return hostAddress
                        }
                    } else {
                        if (!isIPv4) {
                            val index = hostAddress!!.indexOf('%')
                            return if (index < 0)
                                hostAddress!!.uppercase(Locale.getDefault())
                            else
                                hostAddress!!.substring(0, index)
                                    .uppercase(Locale.getDefault())
                        }
                    }
                }
            }
        }
    } catch (e: SocketException) {
        e.printStackTrace()
    }
    return null
}


/**
 * 生成设备唯一标识：IMEI、AndroidId、macAddress 三者拼接再 MD5
 */
//@RequiresPermission("android.permission.READ_PRIVILEGED_PHONE_STATE")
//fun getUniqueDeviceID2MD5(context: Context): String? {
//    var imei = ""
//    var androidId = ""
//    var macAddress = ""
//    val telephonyManager = context.getSystemService(
//        TELEPHONY_SERVICE
//    ) as TelephonyManager
//    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) !=
//        PackageManager.PERMISSION_GRANTED
//    ) {
//        //    ActivityCompat#requestPermissions
//        // here to request the missing permissions, and then overriding
//        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//        //                                          int[] grantResults)
//        // to handle the case where the user grants the permission. See the documentation
//        // for ActivityCompat#requestPermissions for more details.
//    }
//    imei = telephonyManager.deviceId
//    val contentResolver = context.contentResolver
//    if (contentResolver != null) {
//        androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
//    }
//    val wifiManager = context.applicationContext
//        .getSystemService(Context.WIFI_SERVICE) as WifiManager
//    if (wifiManager != null) {
//        macAddress = wifiManager.connectionInfo.macAddress
//    }
//    val longIdBuilder = StringBuilder()
//    if (imei != null) {
//        longIdBuilder.append(imei)
//    }
//    if (androidId != null) {
//        longIdBuilder.append(androidId)
//    }
//    if (macAddress != null) {
//        longIdBuilder.append(macAddress)
//    }
//    return encryptMD32Lower(longIdBuilder.toString())
//}




/**
 * 是否联网
 * @param TRANSPORT_WIFI -- Wi-Fi 连接
 * @param TRANSPORT_CELLULAR -- 蜂窝数据连接
 * @param TRANSPORT_ETHERNET -- 以太网连接
 * @param TRANSPORT_BLUETOOTH -- 蓝牙网络连接
 *
 */
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun isNetworkConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true // API >= 31
        else -> false
    }
}