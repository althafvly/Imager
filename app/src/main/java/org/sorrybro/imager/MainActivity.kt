package org.sorrybro.imager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import org.sorrybro.imager.utils.ImageUtils
import org.sorrybro.imager.utils.NetworkUtils

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        imageView = findViewById(R.id.imageView)

        button.setOnClickListener {
            if (NetworkUtils.isNetworkAvailable(this)) {
                ImageUtils.loadImage(imageView, this@MainActivity)
            } else {
                Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        val saved = ImageUtils.loadSavedImage(imageView, this)
        if (saved) {
            Toast.makeText(this, R.string.saved_image, Toast.LENGTH_LONG).show()
        }
        super.onResume()
    }
}