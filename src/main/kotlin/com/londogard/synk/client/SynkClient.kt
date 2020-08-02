package com.londogard.synk.client

import com.londogard.synk.server.Port
import com.londogard.synk.utils.createTarZstd
import java.io.File
import java.io.FileInputStream
import java.net.InetSocketAddress
import java.nio.channels.SocketChannel

class SynkClient {
    fun client(filename: String) {
        println("Client::init")
        val serverAddress = InetSocketAddress(Port)
        createTarZstd(File(filename), File("$filename.tar.szt"))
        val fileChannel = FileInputStream("$filename.tar.szt").channel

        println("Client::Connecting to Server")
        val socketChannel = SocketChannel.open(serverAddress)
        println("Client::Sending file $filename")
        fileChannel.transferTo(0, fileChannel.size(), socketChannel)

        println("Client::Finished, shutting done")
        socketChannel.close()
        fileChannel.close()
        println("Client::Closed")
    }
}
