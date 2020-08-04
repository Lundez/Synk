package com.londogard.synk.client

import com.londogard.synk.discoveryservice.DiscoverService
import com.londogard.synk.utils.CompressionUtil
import com.londogard.synk.utils.CompressionUtil.suffix
import java.io.FileOutputStream
import java.net.InetSocketAddress
import java.nio.channels.FileChannel
import java.nio.channels.SocketChannel
import java.nio.file.Paths

object SynkClient {
    fun client(connectionCode: String) {
        println("Client::init")
        val connectionCodeData = DiscoverService.connectionCodeData(connectionCode)
        val serverAddress = InetSocketAddress(connectionCodeData.ip, connectionCodeData.port)

        val compressPath = Paths.get("${connectionCodeData.filename}$suffix")
        val fileChannel: FileChannel = FileOutputStream(compressPath.toFile()).channel

        println("Client::Connecting to Server")
        val socketChannel = SocketChannel.open(serverAddress)

        println("Client::Accepting file ${connectionCodeData.filename}")
        fileChannel.transferFrom(socketChannel, 0, Long.MAX_VALUE) // Create for-loop
        fileChannel.close()
        CompressionUtil.decompressTarZst(compressPath)
        compressPath.toFile().delete()
        println("Client::File found in ${compressPath.parent ?: Paths.get("").toAbsolutePath()}")
        println("Client::Finished, shutting done.")
        socketChannel.close()
        println("Client::Closed")
    }
}
