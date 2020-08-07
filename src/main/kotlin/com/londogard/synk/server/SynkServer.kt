package com.londogard.synk.server

import com.londogard.synk.discoveryservice.DiscoverService
import com.londogard.synk.discoveryservice.DiscoverService.Port
import com.londogard.synk.utils.CompressionUtil
import com.londogard.synk.utils.CompressionUtil.compress
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.nio.file.Paths


object SynkServer {
    fun server(filename: String): Unit = runCatching {
        println("Server::init")

        val inetAddress = InetSocketAddress(DiscoverService.getLanIP(), Port)
        println("Server::Compressing file $filename")
        val filePath = Paths.get(filename)
        val path = filePath.compress()

        val serverSocketChannel = ServerSocketChannel.open().apply { bind(inetAddress) }

        println("Server::Connection Code = ${DiscoverService.getConnectionCode(filePath)}")
        val socketChannel = serverSocketChannel.accept()
        println("Server::Accepted Client")

        val fileChannel = path.toFile().inputStream().channel

        println("Server::Sending file $filename")
        var remaining = fileChannel.size()
        var position = 0L
        while (remaining > 0) { // Looping to make sure > 2GB files also works..
            val transferred = fileChannel.transferTo(position, remaining, socketChannel)
            remaining -= transferred
            position += transferred
        }

        println("Server::Finished, shutting done")
        socketChannel.close()
        serverSocketChannel.close()
        fileChannel.close()
        path.toFile().delete()
        println("Server::Closed")
    }
        .onFailure { exception -> println("Server::CRASH ${exception.message}.\n\nStacktrace:\n${exception.stackTrace}") }
        .getOrDefault(Unit)
}