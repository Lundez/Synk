package com.londogard.synk

import com.github.ajalt.clikt.completion.CompletionCandidates
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import com.londogard.synk.client.SynkClient
// import com.londogard.synk.client.SynkWebClient
import com.londogard.synk.server.SynkServer
import kotlinx.coroutines.*
import java.io.File
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

class Synk : CliktCommand() {
    override fun run() = Unit
}

class ClientCommands : CliktCommand(name = "client") {
    private val code: String by argument(help = "The code to open communicaton with server")

    override fun run() {
        SynkClient.client(code)
    }

}

class ServerCommands : CliktCommand(name = "server") {
    private val file: File by argument(
        help = "The file/directory to send to client",
        completionCandidates = CompletionCandidates.Path
    )
        .file(mustExist = true)

    override fun run() {
        SynkServer.server(file.absolutePath)
    }
}

fun socketBasedSynk() = runBlocking {
    async(Dispatchers.IO) {
        SynkServer.server("src")
    }
    delay(5000)
    async(Dispatchers.Default) {
        SynkClient.client("synk:192.168.0.171:31415:src")
    }
}
/**
fun httpBasedSynk() = runBlocking {
    val a = async(Dispatchers.IO) {
        SynkWebClient.webServer()
    }
    delay(5000)
    async(Dispatchers.Default) {
        SynkWebClient.webClient()
    }.invokeOnCompletion { exitProcess(0) }
}**/

fun main() {
    measureTimeMillis {
        if (true) socketBasedSynk()
    }
        .also { println("Time taken $it ms") }
}
