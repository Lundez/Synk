package com.smor.synk.utils

/**
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun createTarGz(`in`: File, out: File) {
    val fOut = FileOutputStream(out)
    val bOut = BufferedOutputStream(fOut)
    val gzOut = GzipCompressorOutputStream(bOut)
    TarArchiveOutputStream(gzOut).use {
        addFileToTarGz(it, `in`, "")
        it.finish()
    }
}

private fun addFileToTarGz(tOut: TarArchiveOutputStream, out: File, base: String) {
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
        out.listFiles()
            ?.forEach {
                addFileToTarGz(tOut, it, entryName + "/")
            }
    }
}
*/