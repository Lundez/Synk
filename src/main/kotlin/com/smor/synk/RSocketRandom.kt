package com.smor.synk

/**
fun main(args: Array<String>) {
RSocketFactory.receive()
// Enable Zero Copy
.frameDecoder(PayloadDecoder.ZERO_COPY)
.acceptor(PingHandler())
.transport(TcpServerTransport.create(7878))
.start()
.block()
.onClose()
.block()
val client = RSocketFactory.connect()
// Enable Zero Copy
.frameDecoder(PayloadDecoder.ZERO_COPY)
.transport(TcpClientTransport.create(7878))
.start()
}
*/