package com.sdk.glassessdksample

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class ObjectDetector {

    private val TAG = "ObjectDetector"

    // Configure ML Kit object detector
    private val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
        .enableMultipleObjects()
        .enableClassification()
        .build()

    private val detector = ObjectDetection.getClient(options)

    /**
     * Detect objects in a bitmap image
     * @param bitmap The image from glasses to analyze
     * @param onResult Callback with list of detected object labels
     */
    fun detectObjects(bitmap: Bitmap, onResult: (List<String>) -> Unit) {
        Log.d(TAG, "Starting object detection on image: ${bitmap.width}x${bitmap.height}")

        val image = InputImage.fromBitmap(bitmap, 0)

        detector.process(image)
            .addOnSuccessListener { detectedObjects ->
                Log.d(TAG, "Detection successful. Found ${detectedObjects.size} objects")

                val labels = mutableListOf<String>()

                detectedObjects.forEach { obj ->
                    // Get the most confident label for each object
                    obj.labels.firstOrNull()?.let { label ->
                        val confidence = (label.confidence * 100).toInt()
                        val labelText = "${label.text} (${confidence}%)"
                        labels.add(labelText)

                        Log.d(TAG, "Detected: $labelText")
                        Log.d(TAG, "  Bounding box: ${obj.boundingBox}")
                    }
                }

                if (labels.isEmpty()) {
                    Log.d(TAG, "No objects with confident labels found")
                    onResult(listOf("No objects detected"))
                } else {
                    onResult(labels)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Object detection failed", e)
                onResult(listOf("Error: ${e.message}"))
            }
    }

    /**
     * Cleanup resources when done
     */
    fun cleanup() {
        try {
            detector.close()
            Log.d(TAG, "ObjectDetector cleaned up")
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning up detector", e)
        }
    }
}