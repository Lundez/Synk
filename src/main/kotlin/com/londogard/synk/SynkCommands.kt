package com.londogard.synk

import com.github.ajalt.clikt.completion.CompletionCandidates
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import com.londogard.synk.client.SynkClient
import com.londogard.synk.server.SynkServer
import java.io.File

class Synk : CliktCommand() {
    override fun run() = Unit
}

class ClientCommands : CliktCommand(name = "client") {
    private val code: String by argument(help="The code to open communicaton with server")

    override fun run() {
        SynkClient.client(code)
    }

}

class ServerCommands : CliktCommand(name="server") {
    private val file: File by argument(help="The file/directory to send to client", completionCandidates = CompletionCandidates.Path)
        .file(mustExist = true)

    override fun run() {
        SynkServer.server(file.absolutePath)
    }
}
