package com.londogard.synk.utils

import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorInputStream
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object CompressionUtil {
    const val suffix = ".tar.zst"

    fun decompressTarZst(`in`: Path, outputPath: Path = `in`.parent ?: Paths.get("")) {
        Files
            .newInputStream(`in`)
            .buffered()
            .let(::ZstdCompressorInputStream)
            .let(::TarArchiveInputStream)
            .use { tarArchive ->
                tarArchive.nextEntry
                while (tarArchive.currentEntry != null) {
                    val path = outputPath.resolve(tarArchive.currentEntry.name)

                    if (tarArchive.currentEntry.isDirectory && !Files.exists(path)) Files.createDirectories(path)
                    else if (tarArchive.currentEntry.isDirectory) Files.createDirectories(path.parent.resolve("copy_synk"))
                    else Files.copy(tarArchive, path)

                    tarArchive.nextEntry
                }
            }

    }

    fun compressTarZst(`in`: Path): Path {
        val path = Paths.get("$`in`$suffix")
        Files
            .newOutputStream(path)
            .buffered()
            .let(::ZstdCompressorOutputStream)
            .let(::TarArchiveOutputStream)
            .use { outputStream ->
                addFileToTar(outputStream, `in`.toFile())
                outputStream.finish()
            }

        return path
    }

    private fun addFileToTar(tOut: TarArchiveOutputStream, `in`: File, base: String = "") {
        val basePath = Paths.get(base, `in`.name).toString()
        val tarEntry = TarArchiveEntry(`in`, basePath)
        tOut.putArchiveEntry(tarEntry)

        if (`in`.isFile) {
            `in`.inputStream().use {
                it.copyTo(tOut)
                tOut.closeArchiveEntry()
            }
        } else {
            tOut.closeArchiveEntry()
            `in`.listFiles()?.forEach {
                addFileToTar(tOut, it, basePath)
            }
        }
    }
}