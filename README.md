# Synk
Sync &amp; Transfer files. Made on the JVM by Londogard.

### TODOs

- [X] CompressionUtil using [Zstandard](https://facebook.github.io/zstd/) + tar to send directories as one single file
- [X] CompressionUtil test
- [X] QR code generation (`ip:port:filename`)
- [X] DiscoveryService (`getLanIP`+`getExternalIP`)
- [X] CLI
- [X] Get a FAT-jar running
- [X] Server
- [X] Client
- [X] Make it "safe" by Try-Catching etc. (using `Result` through `runCatching`)
- [ ] Add support for [rsocket/rsocket-kotlin](https://github.com/rsocket/rsocket-kotlin)
- [ ] HTTPS / WebDAV : https://ktor.io/servers/features/websockets.html#Frame
- [ ] DiscoveryService auto-connect (pinging or something, on same LAN.)
- [ ] Kotlin MPP with KotlinJS frontend! (fritz2?)
- [ ] Route through the RaspPi
- [ ] Investigate if perhaps http4k, Netty, KTOR, Micronaut, Fuel could work. 
- [ ] QR code "deep link"
- [ ] Android App
- [ ] GUI (...or should it exist even?)
- [ ] Tests
- [ ] HTTP4K, or Fuel??? (Pretty clean HTTP code!)
- [ ] Coroutine + Sockets? See [here](https://stackoverflow.com/questions/53736127/how-to-implement-nio-socket-client-using-kotlin-coroutines-in-java-code)
- [ ] ~~Get GraalVM native image to work (?). Otherwise FATJar will do.~~ (doesn't work 'cus of zstd-jni... WIP)

Potentially; Use RPi + KTOR to create a zero-copy transfer between buffers/clients?

### Advanced / Awesome mode TODOs:
* Packet handling by cases
* Encryption
* GraalVM
* 24/7 (GraalVM could be key)
* Off-loading to a server (Pi) if someone is not online
* Smart Discovery (small code, whatevs)
* Add Chromecast support, because why not? #legacy

**Finally:** WC3 Enfos!!!!

https://examples.javacodegeeks.com/core-java/nio/java-nio-large-file-transfer-tutorial/
https://fracpete.github.io/rsync4j/java/
https://github.com/xerial/larray