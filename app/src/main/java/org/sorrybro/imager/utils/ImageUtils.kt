package org.sorrybro.imager.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors


class ImageUtils {

    companion object {
        private const val baseUrl = "https://picsum.photos/200"
        private const val lastSaveImage = "temp.jpg"

        fun loadImage(
            imageView: ImageView,
            context: Context
        ) {
            /**
             * Loads a random image from Lorem Picsum website
             */
            Executors.newSingleThreadExecutor().execute {
                // Tries to get the image and post it in the ImageView
                // Save image after posted in ImageView
                // with the help of Handler
                try {
                    val `in` = java.net.URL(baseUrl).openStream()
                    val image = BitmapFactory.decodeStream(`in`)

                    // Only for making changes in UI
                    Handler(Looper.getMainLooper()).post {
                        imageView.setImageBitmap(image)

                        // Save the image from the ImageView
                        saveImage(imageView, context)
                    }
                }

                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        private fun saveImage(imageView: ImageView, context: Context) {
            /**
             * Save the loaded image
             */
            val bitmap = imageView.drawable.toBitmap()
            val file = File(context.dataDir.toString(), lastSaveImage)
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
        }

        fun loadSavedImage(imageView: ImageView, context: Context): Boolean {
            /**
             * Load the last saved image
             */
            val file = File(context.dataDir.toString(), lastSaveImage)
            if (file.exists()) {
                val image = BitmapFactory.decodeFile(file.path)
                imageView.setImageBitmap(image)
                return true
            }
            return false
        }
    }
}