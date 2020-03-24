package com.smor.synk.discoveryservice
/**
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.NetworkInterface
import java.net.URL

fun getLanIP(): String =
    NetworkInterface.getNetworkInterfaces().toList()
        .filter { !it.isLoopback }
        .flatMap { it.inetAddresses.toList() }
        .map { it.hostAddress }
        .first { it.startsWith("192") }

fun getExternalIP(): String = try {
    // https works too?
    val checkIpStream = URL("http://checkip.amazonaws.com").openStream()
    val reader = BufferedReader(InputStreamReader(checkIpStream))

    reader.readLine()
} catch (exception: IOException) {
    "Can't reach checkip.amazonaws.com to verify IP"
}
*/