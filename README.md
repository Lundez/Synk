# Synk
Sync &amp; Transfer files. Made on the JVM by Smør.

What we need:

1. Client
2. Server
3. DiscoveryService
4. CompressionUtil: Make use of [Zstandard](https://facebook.github.io/zstd/)? 
5. Tests!

What we need to think about.  
* Selection of client (KTOR to begin with, then moving to a lower level perhaps?)
* Selection of server (same as above?)
* Merge Server/Client and only have one type?
* _Optimizations_

Advanced / Awesome mode:
* Packet handling by cases
* Encryption
* GraalVM
* 24/7 (GraalVM could be key)
* Off-loading to a server (Pi) if someone is not online
* Smart Discovery (small code, whatevs)
* Add Chromecast support, because why not? #legacy

**Finally:** WC3 Enfos!!!!