package org.sorrybro.imager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import org.sorrybro.imager.databinding.ActivityMainBinding
import org.sorrybro.imager.utils.ImageUtils
import org.sorrybro.imager.utils.NetworkUtils


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            if (NetworkUtils.isNetworkAvailable(this)) {
                ImageUtils.loadImage(binding.imageView, this@MainActivity)
            } else {
                Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        if (ImageUtils.loadSavedImage(binding.imageView, this)) {
            Toast.makeText(this, R.string.saved_image, Toast.LENGTH_LONG).show()
        }
        super.onResume()
    }
}