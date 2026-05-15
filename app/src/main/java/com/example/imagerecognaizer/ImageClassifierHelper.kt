package com.example.imagerecognaizer

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ImageProcessor

import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.task.vision.classifier.Classifications



class ImageClassifierHelper(context: Context) {
    private val classifier: ImageClassifier

    init {
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setMaxResults(5)
            .setScoreThreshold(0.1f)
            .build()

        classifier = ImageClassifier.createFromFileAndOptions(
            context,
            "model.tflite",
            options
        )
    }

    fun classify(bitmap: Bitmap): List<Category> {
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(299, 299, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        val tensorImage = TensorImage.fromBitmap(bitmap)
        val processedImage = imageProcessor.process(tensorImage)

        val results = classifier.classify(processedImage)
        return results.first().categories
            .sortedByDescending { it.score }
            .take(5)
    }
}