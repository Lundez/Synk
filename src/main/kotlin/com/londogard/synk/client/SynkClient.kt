package com.londogard.synk.client

import com.londogard.synk.discoveryservice.DiscoverService
import com.londogard.synk.utils.CompressionUtil.decompress
import com.londogard.synk.utils.CompressionUtil.suffix
import java.net.InetSocketAddress
import java.nio.channels.FileChannel
import java.nio.channels.SocketChannel
import java.nio.file.Paths

object SynkClient {
    fun client(connectionCode: String): Unit = runCatching {
        println("Client::init")
        val connectionCodeData = DiscoverService.connectionCodeData(connectionCode)
        val serverAddress = InetSocketAddress(connectionCodeData.ip, connectionCodeData.port)

        val compressPath = Paths.get("synk_${connectionCodeData.filename}${suffix}")
        val fileChannel: FileChannel = compressPath.toFile().outputStream().channel

        println("Client::Connecting to Server")
        val socketChannel = SocketChannel.open(serverAddress)

        println("Client::Accepting file ${connectionCodeData.filename}")
        var offset: Long = 0

        var count: Long
        while (fileChannel.transferFrom(socketChannel, offset, Long.MAX_VALUE).also { count = it } > 0) {
            offset += count
        }

        fileChannel.close()
        compressPath.decompress()

        compressPath.toFile().delete()
        println("Client::File found in ${compressPath.parent ?: Paths.get("").toAbsolutePath()}")
        println("Client::Finished, shutting done.")
        socketChannel.close()
        println("Client::Closed")
    }
        .onFailure { exception -> println("Client::CRASH ${exception.message}.\n\nLocalized:\n${exception.printStackTrace()}") }
        .getOrDefault(Unit)
}