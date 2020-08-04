# Synk
Sync &amp; Transfer files. Made on the JVM by Londogard.

### TODOs

- [X] CompressionUtil using [Zstandard](https://facebook.github.io/zstd/) + tar to send directories as one single file
- [X] CompressionUtil test
- [X] QR code generation (`ip:port:filename`)
- [X] DiscoveryService (`getLanIP`+`getExternalIP`)
- [X] CLI
- [ ] GUI
- [ ] Get GraalVM native image to work (?). Otherwise FATJar will do.
- [ ] DiscoveryService auto-connect (pinging or something, on same LAN.)
- [ ] Server
- [ ] Client
- [ ] Investigate if perhaps KTOR or some lambda framework could be a good fit, rather than raw sockets. 
- [ ] QR code "deep link"
- [ ] Android App
- [ ] Tests

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