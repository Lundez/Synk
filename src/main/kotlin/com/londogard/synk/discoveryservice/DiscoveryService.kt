package com.londogard.synk.discoveryservice

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.URL

object DiscoverService {
    fun getLanIP(): String =
        NetworkInterface.getNetworkInterfaces().toList()
            .filterNot(NetworkInterface::isLoopback)
            .flatMap { it.inetAddresses.toList() }
            .map(InetAddress::getHostAddress)
            .first { ip -> ip.startsWith("192") }

    fun getExternalIP(): String = try {
        URL("https://ipconfig.io/ip")
            .openStream()
            .reader()
            .use { readLine() } ?: getLanIP()
    } catch (exception: IOException) {
        "Can't reach checkip.amazonaws.com to verify IP"
    }
}

// https://developer.ibm.com/technologies/java/articles/j-zerocopy/