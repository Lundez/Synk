# Synk
Sync &amp; Transfer files. Made on the JVM by Londogard.

### TODOs

- [X] CompressionUtil using [Zstandard](https://facebook.github.io/zstd/) + tar to send directories as one single file
- [ ] DiscoveryService (finished `getLanIP`+`getExternalIP` but still no handshake)
- [ ] Server
- [ ] Client
- [ ] Investigate if perhaps KTOR or some lambda framework could be a good fit, rather than raw sockets.
- [ ] QR code generation + "deep-link generation"
- [ ] Android App
- [ ] Tests


### Advanced / Awesome mode TODOs:
* Packet handling by cases
* Encryption
* GraalVM
* 24/7 (GraalVM could be key)
* Off-loading to a server (Pi) if someone is not online
* Smart Discovery (small code, whatevs)
* Add Chromecast support, because why not? #legacy

**Finally:** WC3 Enfos!!!!