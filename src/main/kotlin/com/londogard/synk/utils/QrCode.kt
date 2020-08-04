package com.londogard.synk.utils

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.londogard.synk.discoveryservice.DiscoverService
import java.nio.file.Path
import java.nio.file.Paths

object QrCode {
    fun create(path: Path): BitMatrix =
        QRCodeWriter().encode(
            DiscoverService.getConnectionCode(path),
            BarcodeFormat.QR_CODE,
            320,
            320
        )

    fun BitMatrix.toPath(path: Path, format: String = "jpg"): Unit =
        MatrixToImageWriter.writeToPath(this, format, path)

    @JvmStatic
    fun main(args: Array<String>) {
        MatrixToImageWriter
            .writeToPath(create(Paths.get("filename")), "jpg", Paths.get("/home/londet/git/synk/src/test/resources/a.jpg"))
    }
}