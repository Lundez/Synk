package com.londogard.synk.utils

import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorInputStream
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object CompressionUtil {
    const val suffix = ".tar.zst"

    private tailrec fun createDirectories(path: Path, index: Int = 0): Path {
        val indexedPath = if (index > 0) path.resolveSibling("${path.fileName}_$index") else path

        return if (Files.exists(indexedPath)) createDirectories(path, index + 1)
        else Files.createDirectories(indexedPath)
    }

    fun Path.decompress(outputPath: Path = this.parent ?: Paths.get("")): Path {
        var finalOutputPath = outputPath

        Files
            .newInputStream(this)
            .buffered()
            .let(::ZstdCompressorInputStream)
            .let(::TarArchiveInputStream)
            .use { tarArchive ->
                tarArchive.nextEntry
                while (tarArchive.currentEntry != null) {
                    val path = finalOutputPath.resolve(tarArchive.currentEntry.name)

                    if (tarArchive.currentEntry.isDirectory)
                        createDirectories(path).also { if (it != path) finalOutputPath = it }
                    else Files.copy(tarArchive, path)

                    tarArchive.nextEntry
                }
            }
        return finalOutputPath
    }

    fun Path.compress(): Path {
        val path = Paths.get("$this$suffix")
        Files
            .newOutputStream(path)
            .let(::ZstdCompressorOutputStream)   // TODO remove to reduce reflection for GraalVM
            .let(::TarArchiveOutputStream)
            .use { outputStream ->
                addFileToTar(outputStream, this.toFile())
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
            `in`.listFiles()?.forEach { childFile -> addFileToTar(tOut, childFile, basePath) }
        }
    }
}