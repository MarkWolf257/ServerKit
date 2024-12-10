package com.markwolf.serverkit.utils

import java.net.NetworkInterface

fun getLocalIPv4(): String? {
    try {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()

        while (networkInterfaces.hasMoreElements()) {
            val networkInterface = networkInterfaces.nextElement()
            val inetAddresses = networkInterface.inetAddresses

            while (inetAddresses.hasMoreElements()) {
                val inetAddress = inetAddresses.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress is java.net.Inet4Address) {
                    return inetAddress.hostAddress
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}