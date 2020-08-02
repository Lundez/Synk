package com.londogard.synk.server

import com.londogard.synk.discoveryservice.DiscoverService
import java.io.FileOutputStream
import java.net.InetSocketAddress
import java.nio.channels.FileChannel
import java.nio.channels.ServerSocketChannel

const val Port = 31415

class SynkServer {
    fun server(filename: String) {
        println("Server::init")
        val inetAddress = InetSocketAddress(DiscoverService.getLanIP(), Port)

        println("${inetAddress.hostName}:${inetAddress.port}") // simplify by using HEX.
        val serverSocketChannel = ServerSocketChannel.open()

        serverSocketChannel.bind(inetAddress)
        val socketChannel = serverSocketChannel.accept()
        println("Server::Accepted Client")

        val fileChannel: FileChannel = FileOutputStream("$filename.tar.szt").channel
        println("Server::Accepting file $filename")
        fileChannel.transferFrom(socketChannel, 0, Long.MAX_VALUE) // Create for-loop
        println("Server::Finished, shutting done")
        socketChannel.close()
        serverSocketChannel.close()
        println("Server::Closed")
    }
}

fun main() {
    SynkServer().server("hello")
}