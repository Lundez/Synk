package com.londogard.synk.utils

import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream
import java.io.*

fun createTarZstd(`in`: File, out: File) {
    val fOut = FileOutputStream(out)
    val bOut = BufferedOutputStream(fOut)

    val zstdOut = ZstdCompressorOutputStream(bOut)
    TarArchiveOutputStream(zstdOut).use {
        addFileToTar(it, `in`, "")
        it.finish()
    }
}

private fun addFileToTar(tOut: TarArchiveOutputStream, out: File, base: String) {
    val entryName = base + out.name
    val tarEntry = TarArchiveEntry(out, entryName)
    tOut.putArchiveEntry(tarEntry)

    if (out.isFile) {
        FileInputStream(out).use {
            it.copyTo(tOut)
            tOut.closeArchiveEntry()
        }
    } else {
        tOut.closeArchiveEntry()
        out.listFiles()?.forEach {
            addFileToTar(tOut, it, "$entryName/${File.pathSeparatorChar}")
        }
    }
}

fun main() {
    createTarZstd(
        File("/home/londet/git/synk"),
        File("/home/londet/A.tar.zstd")
    )
}