package com.londogard.synk.server

import com.londogard.synk.discoveryservice.DiscoverService
import com.londogard.synk.discoveryservice.DiscoverService.Port
import com.londogard.synk.utils.CompressionUtil
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.nio.file.Paths

object SynkServer {
    fun server(filename: String) {
        println("Server::init")
        val inetAddress = InetSocketAddress(DiscoverService.getLanIP(), Port)
        println("Server::Compressing file $filename")
        val filePath = Paths.get(filename)
        val path = CompressionUtil.compressTarZst(filePath)

        println(DiscoverService.getConnectionCode(filePath))
        val serverSocketChannel = ServerSocketChannel.open().apply { bind(inetAddress) }

        println("Server::Connection Code = ${DiscoverService.getConnectionCode(filePath)}")
        val socketChannel = serverSocketChannel.accept()
        println("Server::Accepted Client")


        val fileChannel = path.toFile().inputStream().channel

        println("Server::Sending file $filename")
        fileChannel.transferTo(0, fileChannel.size(), socketChannel)


        println("Server::Finished, shutting done")
        socketChannel.close()
        serverSocketChannel.close()
        fileChannel.close()
        path.toFile().delete()
        println("Server::Closed")
    }
}