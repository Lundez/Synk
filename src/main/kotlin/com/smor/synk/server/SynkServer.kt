package com.smor.synk.server

class SynkServer {
    /**
    import java.io.FileOutputStream
    import java.net.InetSocketAddress
    import java.nio.channels.FileChannel
    import java.nio.channels.ServerSocketChannel

    private const val Port = 31415

    fun server(filename: String) {
    println("Server::init")
    val inetAddress = InetSocketAddress(Port)
    val serverSocketChannel = ServerSocketChannel.open()

    serverSocketChannel.bind(inetAddress)
    val socketChannel = serverSocketChannel.accept()
    println("Server::Accepted Client")

    val fileChannel: FileChannel = FileOutputStream("$filename.zip").channel
    println("Server::Accepting file $filename")
    fileChannel.transferFrom(socketChannel, 0, Long.MAX_VALUE) // Create for-loop
    println("Server::Finished, shutting done")
    socketChannel.close()
    serverSocketChannel.close()
    println("Server::Closed")
    }
     */
}