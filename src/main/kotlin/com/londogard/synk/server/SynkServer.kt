package com.londogard.synk.server

import com.londogard.synk.discoveryservice.DiscoverService
import com.londogard.synk.discoveryservice.DiscoverService.Port
import com.londogard.synk.utils.CompressionUtil
import com.londogard.synk.utils.CompressionUtil.compress
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.RSocketRequestHandler
import io.rsocket.kotlin.core.RSocketServerSupport
import io.rsocket.kotlin.core.rSocket
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.plugin.Plugin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.nio.file.Paths


object SynkServer {
    fun rsocketServer(filename: String): Unit = runCatching {
        //create ktor server
        embeddedServer(CIO) {
            install(RSocketServerSupport) {
                //configure rSocket server (all values have defaults)

                //install interceptors
                plugin = Plugin(
                    //connection = listOf(::SomeConnectionInterceptor)
                )
            }
            //configure routing
            routing {
                //configure route `url:port/rsocket`
                rSocket("rsocket") {
                    RSocketRequestHandler {
                        //handler for request/response
                        requestResponse = { request: Payload ->
                            //... some work here
                            delay(500) // work emulation
                            Payload("data", "metadata")
                        }
                        //handler for request/stream
                        requestStream = { request: Payload ->
                            flow {
                                repeat(1000) { i ->
                                    emit(Payload("data: $i"))
                                }
                            }
                        }
                    }
                }
            }
        }.start(true)
        Unit
    }
        .onFailure { exception -> println("Server::CRASH ${exception.message}.\n\nStacktrace:\n${exception.stackTrace}") }
        .getOrDefault(Unit)

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