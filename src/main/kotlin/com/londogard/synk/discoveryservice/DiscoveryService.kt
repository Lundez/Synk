package com.londogard.synk.discoveryservice

import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.URL
import java.nio.file.Path

data class ConnectionCode(val ip: String, val port: Int, val filename: String)

object DiscoverService {
    const val Port = 31415

    fun getConnectionCode(path: Path): String =
        "synk:${getLanIP()}:$Port:${path.fileName}"

    fun connectionCodeData(connectionCode: String): ConnectionCode = connectionCode
        .split(':')
        .drop(1)
        .let { codeSplit -> ConnectionCode(codeSplit[0], codeSplit[1].toInt(), codeSplit[2]) }

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