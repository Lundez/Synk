package com.londogard.synk.client

import com.londogard.synk.discoveryservice.DiscoverService
import com.londogard.synk.utils.CompressionUtil
import com.oracle.webservices.internal.api.message.ContentType
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Text
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpStatement
import io.ktor.http.URLProtocol.Companion.HTTPS
import io.ktor.http.cio.parseMultipart
import io.ktor.http.content.MultiPartData
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.cio.readChannel
import io.ktor.util.cio.use
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.copyAndClose
import io.ktor.utils.io.copyTo
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.io.File
import java.nio.file.Paths
import kotlin.system.measureTimeMillis


@KtorExperimentalAPI
object SynkWebClient {
    suspend fun webClient() {
        val client = HttpClient(CIO) {
            install(WebSockets)
        }
        println("Client open")
        measureTimeMillis {
            client
                .get<HttpStatement> {
                    url("http://127.0.0.1:${DiscoverService.Port}")
                }
                .execute { response ->
                    measureTimeMillis { File("synk2").writeChannel().use {
                        response.content.copyAndClose(this)
                    } }.also { println(it) }
                }
        }.also { println("Downloaded in $it ms") }

    }

    fun webServer() {
        embeddedServer(Netty, DiscoverService.Port) {
            routing {
                get("/") {
                    call.respondFile(File("src/sv_300d.tsv.tar.zst"))
                }
            }
        }.start(wait = true)
    }
}