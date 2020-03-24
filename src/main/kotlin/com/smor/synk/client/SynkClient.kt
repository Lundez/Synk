package com.smor.synk.client

import java.io.FileInputStream
import java.net.InetSocketAddress
import java.nio.channels.SocketChannel

class SynkClient {
    /**
    fun client(filename: String) {
        println("Client::init")
        val serverAddress = InetSocketAddress(Port)
        pack(filename, "$filename.zip")
        val fileChannel = FileInputStream("$filename.zip").channel

        println("Client::Connecting to Server")
        val socketChannel = SocketChannel.open(serverAddress)
        println("Client::Sending file $filename")
        fileChannel.transferTo(0, fileChannel.size(), socketChannel)

        println("Client::Finished, shutting done")
        socketChannel.close()
        fileChannel.close()
        println("Client::Closed")
    }
     */
}
