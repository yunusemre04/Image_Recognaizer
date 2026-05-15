package com.example.imagerecognaizer.cameraUtils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.imagerecognaizer.ImageClassifierHelper
import java.io.ByteArrayOutputStream


class RealTimeImageAnalyzer(
    private val context: Context,
    private val onResult: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val classifier = ImageClassifierHelper(context)
    @Volatile private var isProcessing = false

    override fun analyze(imageProxy: ImageProxy) {
        if (isProcessing) {
            imageProxy.close()
            return
        }

        isProcessing = true

        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
        val bitmap = imageProxy.toBitmap(rotationDegrees)

        bitmap?.let {
            val resultList = classifier.classify(it)
            val topLabel = resultList.firstOrNull()
            if (topLabel != null) {
                onResult(topLabel.label)
            }
        }

        imageProxy.close()
        isProcessing = false
    }

    // ... toBitmap() aynı kalabilir, sadece filter=true eklersin:
    private fun ImageProxy.toBitmap(rotationDegrees: Int): Bitmap? {
        val yBuffer = planes[0].buffer
        val uBuffer = planes[1].buffer
        val vBuffer = planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
        val yuv = out.toByteArray()

        // Asıl burada bitmap tanımlanıyor
        var bitmap = BitmapFactory.decodeByteArray(yuv, 0, yuv.size)

        // Ölçeklendirme sırasında filtreleme açıldı
        bitmap = Bitmap.createScaledBitmap(bitmap, 299, 299, true)

        val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

}
