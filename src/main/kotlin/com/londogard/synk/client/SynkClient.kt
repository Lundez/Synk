package com.londogard.synk.client

import com.londogard.synk.discoveryservice.DiscoverService
import com.londogard.synk.utils.CompressionUtil.decompress
import com.londogard.synk.utils.CompressionUtil.suffix
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.util.*
import io.rsocket.kotlin.RSocketRequestHandler
import io.rsocket.kotlin.core.RSocketClientSupport
import io.rsocket.kotlin.keepalive.KeepAlive
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.PayloadMimeType
import java.net.InetSocketAddress
import java.nio.channels.FileChannel
import java.nio.channels.SocketChannel
import java.nio.file.Paths
import kotlin.time.ExperimentalTime
import kotlin.time.minutes
import kotlin.time.seconds

object SynkClient {
    @KtorExperimentalAPI
    @ExperimentalTime
    fun rsocketClient(connectionCode: String): Unit = runCatching {
        val client = HttpClient(CIO) {
            install(WebSockets)
            install(RSocketClientSupport) {
                //configure rSocket client (all values have defaults)

                keepAlive = KeepAlive(
                    interval = 30.seconds,
                    maxLifetime = 2.minutes
                )

                //payload for setup frame
                setupPayload = Payload(config = TODO("TODO"))

                //mime types
                payloadMimeType = PayloadMimeType(
                    data = "application/json",
                    metadata = "application/json"
                )

                //optional acceptor for server requests
                acceptor = {
                    RSocketRequestHandler {
                        requestResponse = { it } //echo request payload
                    }
                }
            }
        }
        TODO("")
    }
        .onFailure { exception -> println("Client::CRASH ${exception.message}.\n\nLocalized:\n${exception.printStackTrace()}") }
        .getOrDefault(Unit)

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