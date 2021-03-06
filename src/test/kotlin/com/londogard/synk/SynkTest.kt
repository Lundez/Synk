package com.londogard.synk

import com.londogard.synk.utils.CompressionUtil.compress
import com.londogard.synk.utils.CompressionUtil.decompress
import com.londogard.synk.utils.CompressionUtil.suffix
import org.junit.Before
import java.nio.file.Paths
import kotlin.test.Test

class SynkTest {
    @Before
    fun initialize() {
        if (javaClass.getResource("/extracted") != null)
            Paths.get(javaClass.getResource("/extracted").toURI()).toFile().deleteRecursively()
    }

    @Test
    fun testCompression() {
        val toCompress = Paths.get(javaClass.getResource("/tocompress").toURI())
        val compressedPath = toCompress.compress()
        assert(javaClass.getResource("/tocompress$suffix") != null)
        compressedPath.decompress(toCompress.parent.resolve("extracted"))
        assert(toCompress.parent.resolve("extracted").toFile().exists())
    }
}